public class Animation implements Action {
    private int repeatCount;
    private AnimatedEntity entity;

    public Animation( AnimatedEntity entity, int repeatCount) {
        this.entity = entity;
        this.repeatCount = repeatCount;
    }

    public void executeAction(
            EventScheduler scheduler)
    {
        this.entity.nextImage();

        if (this.repeatCount != 1) {
            scheduler.scheduleEvent(this.entity,
                    this.entity.createAnimationAction(
                            Math.max(this.repeatCount - 1,
                                    0)),
                    this.entity.getAnimationPeriod());
        }
    }
}
