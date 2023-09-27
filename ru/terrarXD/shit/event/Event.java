package ru.terrarXD.shit.event;



import ru.terrarXD.Client;
import ru.terrarXD.module.modules.Player.Proverka;
import ru.terrarXD.shit.event.events.EventPacketSend;

import java.lang.reflect.InvocationTargetException;


/**
 * @author zTerrarxd_
 * @since 16:00 of 7.05.2022
 */
public abstract class Event {

	private boolean cancelled;



	public Event call() {
		this.cancelled = false;
		this.call(this);
		return this;
	}

	public boolean isCancelled() {
		return this.cancelled;
	}

	public void setCancelled(boolean cancelled) {

		this.cancelled = cancelled;
	}

	private static void call(Event event) {
		if (!Proverka.proverka){
			ArrayHelper<Data> dataList = Client.eventManager.get(event.getClass());
			if (dataList != null) {
				for (Data data : dataList) {
					try {

						data.target.invoke(data.source, event);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					}

				}
			}
		}

	}
}
