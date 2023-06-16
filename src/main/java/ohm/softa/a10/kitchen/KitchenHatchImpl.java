package ohm.softa.a10.kitchen;

import ohm.softa.a10.model.Dish;
import ohm.softa.a10.model.Order;

import java.util.LinkedList;
import java.util.Deque;

public class KitchenHatchImpl implements KitchenHatch {

	int maxMeals;

	final Deque<Order> orders;

	final Deque<Dish> dishes = new LinkedList<>();

	public KitchenHatchImpl(int maxMeals, Deque<Order> orders) {
		this.maxMeals = maxMeals;
		this.orders = orders;
	}

	@Override
	public int getMaxDishes() {
		return this.maxMeals;
	}

	@Override
	public Order dequeueOrder(long timeout) {
		synchronized (orders) {
			return orders.pop();
		}
	}

	@Override
	public int getOrderCount() {
		synchronized (orders) {
			return orders.size();
		}
	}

	@Override
	public Dish dequeueDish(long timeout) {

		long currentTime = System.nanoTime();

		synchronized (dishes) {
			while(dishes.size() == 0) {
				try {
					dishes.wait(timeout);
				}catch (InterruptedException e){
					e.printStackTrace();
				}

				if(timeout>0&& dishes.size() == 0 && System.nanoTime() - currentTime >= timeout) {
					dishes.notifyAll();
					return null;
				}
			}

			Dish d = dishes.pop();
			dishes.notifyAll();
			return d;
		}
	}

	@Override
	public  void enqueueDish(Dish m) {
		synchronized (dishes) {
			dishes.push(m);
		}
	}

	@Override
	public int getDishesCount() {
		synchronized (dishes) {
			return dishes.size();
		}
	}
}
