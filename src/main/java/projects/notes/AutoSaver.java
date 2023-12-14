package projects.notes;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class AutoSaver {
    private static final Duration DEBOUNCE_DELAY = Duration.seconds(1);
    private final Timeline debounceTimeline;

    private final Runnable autoSaveAction;

    public AutoSaver(Runnable autoSaveAction) {
        this.autoSaveAction = autoSaveAction;
        debounceTimeline = new Timeline(new KeyFrame(DEBOUNCE_DELAY, event -> autoSave()));
        debounceTimeline.setCycleCount(1);
    }

    public void contentChanged() {
        debounceTimeline.stop();
        debounceTimeline.playFromStart();
    }

    private void autoSave() {
        autoSaveAction.run();
    }
}
