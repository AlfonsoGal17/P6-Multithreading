// Insert header here
package assignment6;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import assignment6.Theater.Seat;
import assignment6.Theater.Ticket;
import java.lang.Thread;

public class BookingClient {
	public ArrayList<String> officeNames = new ArrayList<String>();
	public ArrayList<Integer> numOfClients = new ArrayList<Integer>();
	public Theater mainTheater;
	public static Integer totalClients = 1;
	public static Integer ticketsLeft;
	public String boxofficeName;
	public int onePass = 0;
	public boolean printed = false;
	public static ArrayList<Integer> clientID = new ArrayList<Integer>();

	/*
	 * @param office maps box office id to number of customers in line
	 * 
	 * @param theater the theater where the show is playing
	 */
	public BookingClient(Map<String, Integer> office, Theater theater) {
		for (Map.Entry<String, Integer> e : office.entrySet()) {
			officeNames.add(e.getKey());
			numOfClients.add(e.getValue());
		}
		mainTheater = theater;
		ticketsLeft = theater.seatList.size();

	}

	/*
	 * Starts the box office simulation by creating (and starting) threads for
	 * each box office to sell tickets for the given theater
	 *
	 * @return list of threads used in the simulation, should have as many
	 * threads as there are box offices
	 */
	public List<Thread> simulate() {
		List<Thread> th = new ArrayList<Thread>();
		// makes the threads
		for (String boxoffice : officeNames) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					synchronized (this) {
						Integer clients = numOfClients.remove(0);
						String name = boxoffice;
						while (clients != 0 && ticketsLeft != 0) {
							Seat s = mainTheater.bestAvailableSeat();
							if (s != null) {
								mainTheater.printTicket(name, s, BookingClient.totalClients);
								clients--;
							}
							// allows for other threads to go too
							try {
								wait(2000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}

						if (ticketsLeft == 0 && printed == false) {
							System.out.println("Sorry, we are sold out.");
							printed = true;
						}
					} // synch
				}
			});
			// keep track of all threads that you make
			th.add(t);
		}
		// start all threads
		for (Thread tt : th) {

			tt.start();
			// let thread sleep so that all threads
			// have started before the first one starts
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		// once done, make threads join so they close
		for (Thread treads : th) {
			try {
				treads.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// return list of all threads
		return th;
	}

	public static void main(String[] args) {
		// initialize theater
		Theater mainShow = new Theater(3, 5, "Jack Reacher: Never Go Back");
		// initializing office
		Map<String, Integer> office = new HashMap<String, Integer>();
		// adds box offices and clients/office
		office.put("BX1", 3);
		office.put("BX3", 3);
		office.put("BX2", 4);
		office.put("BX5", 3);
		office.put("BX4", 3);
		// parse data
		BookingClient bc = new BookingClient(office, mainShow);
		// process tickets based on passed data
		bc.simulate();
		// test printing transaction log
		System.out.println("\n\n\nPrinting Purchase History...");
		List<Ticket> tLog = bc.mainTheater.getTransactionLog();
		for (Ticket log : tLog) {
			log.toString();
		}

	}

}
