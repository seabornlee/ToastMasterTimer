package cn.seabornlee.toastmastertimer;

import android.os.CountDownTimer;

public class ToastMasterTimer {
    private int minutes;
    private CountDownTimer timer;
    private TimerListener timerListener;

    public ToastMasterTimer(TimerListener timerListener) {
        this.timerListener = timerListener;
    }

    private boolean isRunning() {
        return timer != null;
    }

    private void cancel() {
        timer.cancel();
        timerListener.onStop();
        timer = null;
        timerListener.showTimer(getMillis());
    }

    private void start() {
        timer = new CountDownTimer(getMillis(), 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                timerListener.showTimer(millisUntilFinished);

                if (isTimeToShowYellowCard(millisUntilFinished)) {
                    timerListener.showYellowCard();
                    return;
                }

                if (isTimeToShowGreenCard(millisUntilFinished)) {
                    timerListener.showGreenCard();
                }
            }

            @Override
            public void onFinish() {
                timerListener.showRedCard();
                timer = new CountDownTimer(getLeeway() * 1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        timerListener.showTimer(millisUntilFinished);
                    }

                    @Override
                    public void onFinish() {
                        timerListener.ringTheBell();
                        timer = null;
                    }
                }.start();
            }
        };
        timer.start();
        timerListener.onStart();
    }

    private int getLeeway() {
        return 15;
    }

    private boolean isTimeToShowYellowCard(long millisUntilFinished) {
        return millisUntilFinished <= 15 * 1000;
    }

    private boolean isTimeToShowGreenCard(long millisUntilFinished) {
        return millisUntilFinished <= 30 * 1000;
    }

    private int getMillis() {
        return minutes * 60 * 1000;
    }

    public void setTimeInMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void toggle() {
        if (isRunning()) {
            cancel();
        } else {
            start();
        }
    }
}
