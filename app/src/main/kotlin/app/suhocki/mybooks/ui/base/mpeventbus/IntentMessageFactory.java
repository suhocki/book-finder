package app.suhocki.mybooks.ui.base.mpeventbus;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Factory class creating {@link IntentMessageProducer}s for all types that can be sent
 * via an {@link Intent}.
 */
class IntentMessageFactory {
    public static IntentMessageProducer get(final CharSequence event) {
        return (name, intent) -> {
            intent.putExtra(name, event);
            intent.putExtra(MPEventBus.MULTI_PROCESS_INTENT_EXTRA_TYPE, MPEventBus.EventType.CHAR_SEQUENCE);
        };
    }

    public static IntentMessageProducer get(final Bundle event) {
        return (name, intent) -> {
            intent.putExtra(name, event);
            intent.putExtra(MPEventBus.MULTI_PROCESS_INTENT_EXTRA_TYPE, MPEventBus.EventType.BUNDLE);
        };
    }

    public static IntentMessageProducer get(final Parcelable event) {
        return (name, intent) -> {
            intent.putExtra(name, event);
            intent.putExtra(MPEventBus.MULTI_PROCESS_INTENT_EXTRA_TYPE, MPEventBus.EventType.PARCELABLE);
        };
    }

    public static IntentMessageProducer get(final Serializable event) {
        return (name, intent) -> {
            intent.putExtra(name, event);
            intent.putExtra(MPEventBus.MULTI_PROCESS_INTENT_EXTRA_TYPE, MPEventBus.EventType.SERIALIZABLE);
        };
    }

    public static IntentMessageProducer get(final String event) {
        return (name, intent) -> {
            intent.putExtra(name, event);
            intent.putExtra(MPEventBus.MULTI_PROCESS_INTENT_EXTRA_TYPE, MPEventBus.EventType.STRING);
        };
    }
}
