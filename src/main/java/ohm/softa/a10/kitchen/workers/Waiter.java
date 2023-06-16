package ohm.softa.a10.kitchen.workers;

import ohm.softa.a10.internals.displaying.ProgressReporter;
import ohm.softa.a10.kitchen.KitchenHatch;
import ohm.softa.a10.model.Dish;

import java.util.Random;

public class Waiter implements Runnable {

	private String name;

	private ProgressReporter progressReporter;

	private KitchenHatch kitchenHatch;

	public Waiter(String name, KitchenHatch kitchenHatch, ProgressReporter progressReporter) {
		this.name = name; this.kitchenHatch = kitchenHatch; this.progressReporter = progressReporter;
	}

	@Override
	public void run() {
		while(kitchenHatch.getDishesCount() > 0 || kitchenHatch.getOrderCount() > 0) {
			Dish dish = kitchenHatch.dequeueDish();
			try {
				Thread.sleep((new Random()).nextInt(1000));
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			progressReporter.updateProgress();
		}
		progressReporter.notifyWaiterLeaving();
	}
}
