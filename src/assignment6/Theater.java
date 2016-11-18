// insert header here
package assignment6;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Theater {
	public static ArrayList<Seat> seatList;
	public static ArrayList<Boolean> emptySeats;
	public static String show;
	public static List<Ticket> ticketLog = new ArrayList<Ticket>();
	/*
	 * Represents a seat in the theater
	 * A1, A2, A3, ... B1, B2, B3 ...
	 */
	static class Seat {
		private int rowNum;
		private int seatNum;

		public Seat(int rowNum, int seatNum) {
			this.rowNum = rowNum;
			this.seatNum = seatNum;
		}

		public int getSeatNum() {
			return seatNum;
		}

		public int getRowNum() {
			return rowNum;
		}

		@Override
		public synchronized  String toString() {
			//this method returns the full Seat location ex: A1
			String s = getAlpha(this.rowNum).toUpperCase() + this.seatNum;
			return s;
		}
	}
	public static String getAlpha(int num) {

	    String result = "";
	    while (num > 0) {
	      num--; // 1 => a, not 0 => a
	      int remainder = num % 26;
	      char digit = (char) (remainder + 97);
	      result = digit + result;
	      num = (num - remainder) / 26;
	    }

	    return result;
	  }
  /*
	 * Represents a ticket purchased by a client
	 */
	static class Ticket {
		private String show;
		private String boxOfficeId;
		private Seat seat;
	  private int client;

		public Ticket(String show, String boxOfficeId, Seat seat, int client) {
			this.show = show;
			this.boxOfficeId = boxOfficeId;
			this.seat = seat;
			this.client = client;
		}

		public Seat getSeat() {
			return seat;
		}

		public String getShow() {
			return show;
		}

		public String getBoxOfficeId() {
			return boxOfficeId;
		}

		public int getClient() {
			return client;
		}

		@Override
		public  String toString() {
			// TODO: Implement this method to return a string that resembles a ticket
			int ticketWidth = 31;
			
			//print top of ticket
			for(int z =0; z< ticketWidth; z++){
				System.out.print("-");
			}
			System.out.println("");
		//line 1
			
			String tempLine = "| Show: " + this.show;
			if(tempLine.length() > 31){
				tempLine = tempLine.substring(0, 27);
				tempLine = tempLine + "...";
			}
			while(tempLine.length() < 30){
				tempLine += " ";
			}
			tempLine += "|";
			System.out.println(tempLine);
		//line 2
			tempLine = "| Box Office ID: " + this.boxOfficeId;
			while(tempLine.length() < 30){
				tempLine += " ";
			}
			tempLine += "|";
			System.out.println(tempLine);
			
		//line 3
			tempLine = "| Seat: " + this.seat.toString();
			while(tempLine.length() < 30){
				tempLine += " ";
			}
			tempLine += "|";
			System.out.println(tempLine);
		//line 4
			tempLine = "| Client: " + this.client;
			while(tempLine.length() <30){
				tempLine += " ";
			}
			tempLine += "|";
			System.out.println(tempLine);
			for(int z =0; z< ticketWidth; z++){
				System.out.print("-");
			}
			System.out.println("");
			//print 
			return null;
		}
	}

	public Theater(int numRows, int seatsPerRow, String show) {
		// TODO: Implement this constructor
		this.show = show;
		seatList = new ArrayList<Seat>();
		emptySeats = new ArrayList<Boolean>();
		for(int i=1; i<numRows+1;i++){
			for(int j =1; j<seatsPerRow+1;j++){
				seatList.add(new Seat(i,j));
				emptySeats.add(false);
			}
		}
		
	}

	/*
	 * Calculates the best seat not yet reserved
	 *
 	 * @return the best seat or null if theater is full
   */
	public   synchronized Seat bestAvailableSeat() {
		//TODO: Implement this method}
		
		for(int i =0; i<emptySeats.size();i++){
			if(emptySeats.get(i)==false){
				emptySeats.set(i, true);
			//	BookingClient.ticketsLeft--;

				return seatList.get(i);
			}
		}
		return null;
	}

	/*
	 * Prints a ticket for the client after they reserve a seat
   * Also prints the ticket to the console
	 *
   * @param seat a particular seat in the theater
   * @return a ticket or null if a box office failed to reserve the seat
   */
	public synchronized Ticket printTicket(String boxOfficeId, Seat seat, int client) {
		
		//TODO: Implement this method
		
		 Ticket ticket = new Ticket(Theater.show,boxOfficeId,seat,client);
	
			
			ticketLog.add(ticket);
			ticket.toString();
			if(seat == null){
				return null;
			}
			BookingClient.totalClients++;
			BookingClient.ticketsLeft--;
		
			return ticket;
		
		
		
	//	return ticket;
	}

	/*
	 * Lists all tickets sold for this theater in order of purchase
	 *
   * @return list of tickets sold
   */
	public synchronized List<Ticket> getTransactionLog() {
		//TODO: Implement this method
		return Theater.ticketLog;
	}
}
