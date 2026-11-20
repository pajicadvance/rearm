package me.pajic.rearm.interpolation;

//? >=26.3 {

import me.pajic.rearm.extension.SteppedPathExtension;
import net.minecraft.core.PositionAndRotation;
import net.minecraft.util.ArrayListDeque;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AbstractInterpolationHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PositionPath;
import net.minecraft.world.entity.PositionStep;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class BackportedSteppedInterpolationHandler extends AbstractInterpolationHandler {
	private final InterpolationData interpolationData = new InterpolationData();
	private final int updateInterval;

	public BackportedSteppedInterpolationHandler(Entity entity, int interpolationSteps) {
		super(entity, interpolationSteps);
		this.updateInterval = entity.getType().updateInterval();
	}

	@Override
	@NotNull
	protected PositionAndRotation.Mutable interpolationData() {
		return interpolationData;
	}

	@Override
	protected void startInterpolating(@NotNull PositionPath position, float yRot, float xRot) {
		if (position instanceof PositionPath.Stepped stepped) {
            SteppedPathExtension ext = (SteppedPathExtension) (Object) stepped;
			if (ext.rearm$startImmediately()) cancel();
		}

		if (!hasActiveInterpolation()) {
			interpolationData.setStartingPoint(entity.position(), entity.getYRot(), entity.getXRot());
		}

		Vec3 endPosition = position.endPosition();
		if (Objects.equals(endPosition, interpolationData.position())) {
			interpolationData.addStep(endPosition, yRot, xRot, updateInterval);
		} else {
			interpolationData.addSteps(position, yRot, xRot, updateInterval);
		}

		interpolationData.set(endPosition, yRot, xRot);
		interpolationData.remainingTicks = interpolationSteps;
	}

	@Override
	protected void doInterpolate() {
		interpolationData.advance(entity.level().getRelativeTickSpeed());
		PositionAndRotation target = interpolationData.currentStepPosRot;
		entity.setPos(target.position());
		entity.setYRot(target.yRot() % 360.0F);
		entity.setXRot(target.xRot() % 360.0F);
	}

	@Override
	public boolean hasActiveInterpolation() {
		return interpolationData.remainingTicks > 0.0F;
	}

	@Override
	public void cancel() {
		interpolationData.reset();
	}

	private static class InterpolationData extends PositionAndRotation.Mutable {
		private final List<Step> remainingSteps = new ArrayListDeque<>();
		private final PositionAndRotation.Mutable currentStepPosRot = new PositionAndRotation.Mutable();
		private float remainingTicks;
		private float totalWeight;

		private void advance(float ticks) {
			float advanceByWeight = ticks * totalWeight / remainingTicks;
			totalWeight -= advanceByWeight;
			remainingTicks -= ticks;
			if (!advanceToNextStep(advanceByWeight)) {
				reset();
			}
		}

		private boolean advanceToNextStep(float advanceByWeight) {
			while (!remainingSteps.isEmpty()) {
				Step step = remainingSteps.getFirst();
				float weight = step.weight;
				if (advanceByWeight < weight) {
					float a = advanceByWeight / weight;
					if (a > 0.0F) {
						currentStepPosRot.set(
								currentStepPosRot.position().lerp(step.position(), a),
								Mth.rotLerp(a, currentStepPosRot.yRot(), step.yRot()),
								Mth.lerp(a, currentStepPosRot.xRot(), step.xRot())
						);
					}

					step.weight -= advanceByWeight;
					return true;
				}

				advanceByWeight -= weight;
				currentStepPosRot.set(step.position(), step.yRot(), step.xRot());
				remainingSteps.removeFirst();
			}

			return false;
		}

		private void reset() {
			remainingSteps.clear();
			remainingTicks = 0.0F;
			totalWeight = 0.0F;
		}

		@Override
		public void addDelta(@NotNull Vec3 delta) {
			super.addDelta(delta);

			for (Step step : remainingSteps) {
				step.addDelta(delta);
			}

			currentStepPosRot.addDelta(delta);
		}

		@Override
		public void addRotation(float yRot, float xRot) {
			super.addRotation(yRot, xRot);

			for (Step step : remainingSteps) {
				step.addRotation(yRot, xRot);
			}

			currentStepPosRot.addRotation(yRot, xRot);
		}

		private void setStartingPoint(Vec3 position, float yRot, float xRot) {
			currentStepPosRot.set(position, yRot, xRot);
		}

		private void addStep(Vec3 position, float yRot, float xRot, int weight) {
			remainingSteps.add(new Step(position, yRot, xRot, weight));
			totalWeight += weight;
		}

		private void addSteps(PositionPath position, float yRot, float xRot, int weight) {
			switch (position) {
				case PositionPath.Linear linear -> addStep(linear.endPosition(), yRot, xRot, weight);
				case PositionPath.Stepped stepped -> {
					List<PositionStep> steps = stepped.steps();
					if (yRot == yRot() && xRot == xRot()) {
						for (PositionStep step : steps) {
							addStep(step.position(), yRot, xRot, step.tickOffset());
						}
						return;
					}

					int totalInterpolationTicks = getInterpolationTicks(steps);
					int offset = 0;

					for (PositionStep step : steps) {
						offset += step.tickOffset();
						float a = (float) offset / totalInterpolationTicks;
						addStep(step.position(), Mth.rotLerp(a, yRot(), yRot), Mth.lerp(a, xRot(), xRot), step.tickOffset());
					}
				}
			}
		}

		private static int getInterpolationTicks(List<PositionStep> steps) {
			int ticks = 0;

			for (PositionStep step : steps) {
				ticks += step.tickOffset();
			}

			return ticks;
		}
	}

	private static class Step extends PositionAndRotation.Mutable {
		private float weight;

		public Step(Vec3 position, float yRot, float xRot, int weight) {
			this.weight = weight;
			set(position, yRot, xRot);
		}
	}
}
//?}
