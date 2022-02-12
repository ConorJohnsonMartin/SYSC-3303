package Assignmnent2;
import java.io.*;
import java.net.*;

/**
 * A class that represents an intermediate connection between the client and server.
 * 
 * @author Conor Johnson Martin 101106217
 * @version February 11th, 2022
 */
public class IntermediateHost {
	
	DatagramSocket receiveSocket, sendSocket, sendReceiveSocket; 
	DatagramPacket clientPacket, serverPacket, responsePacket, clientResponsePacket;
	
	/**
	 * Default constructor for the IntermediateHost class that initializes sockets
	 */
	public IntermediateHost() {
		try {
			sendSocket = new DatagramSocket();
			receiveSocket = new DatagramSocket(23);
			sendReceiveSocket = new DatagramSocket();
		}catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * A method that will receive a packet from the Client and forward the packet to the Server.
	 * Then the method will receive a response packet from the Server (permitted no exceptions) and forward the response packet to the Client.
	 * @throws IOException If there is an issue with I/O.
	 * @throws InterruptedException If there is an issue from sleep.
	 */
	public void sendAndReceive() throws IOException, InterruptedException {
		//Receive packet from Client
		byte[] packet1 = new byte[32];
		clientPacket = new DatagramPacket(packet1, packet1.length);
		try {
			System.out.println("Host waiting for client packet...");
			receiveSocket.receive(clientPacket);
			Thread.sleep(5);
			
			System.out.print("Host - Received Packet from Client (Bytes): ");
			for(int i = 0; i < packet1.length; i++) {
				System.out.print(packet1[i]+ " ");
			}
			System.out.println("\n" + "Host - Received Packet from Client (String): " + (new String(packet1)));
		}catch(SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
		
		//Forward packet to Server
		try {
			responsePacket = new DatagramPacket(packet1, packet1.length, InetAddress.getLocalHost(), 69);
		}catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		try {
			Thread.sleep(5);
			sendReceiveSocket.send(responsePacket);
			System.out.print("Host - Send Packet to Server (Bytes): ");
			for(int i = 0; i < packet1.length; i++) {
				System.out.print(packet1[i]+ " ");
			}
			System.out.println("\n" + "Host - Send Packet to Server (String): " + (new String(packet1)));
		}catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		//Receive packet from Server
		byte[] packet2 = new byte[4];
		serverPacket = new DatagramPacket(packet2, packet2.length);
		try {
			Thread.sleep(5);
			System.out.println("Host waiting for server packet...");
			sendReceiveSocket.receive(serverPacket);
			Thread.sleep(5);
			
			System.out.print("Host - Received Server packet (Bytes): ");
			for(int i = 0; i < packet2.length; i++) {
				System.out.print(packet2[i]+ " ");
			}
			System.out.print("\n");
		}catch(SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
		
		//Forward packet to Client
		clientResponsePacket = new DatagramPacket(packet2, packet2.length, clientPacket.getAddress(), clientPacket.getPort());
		try {
			Thread.sleep(5);
			sendSocket.send(clientResponsePacket);
			System.out.print("Host - Send Packet to Client (Bytes): ");
			for(int i = 0; i < packet2.length; i++) {
				System.out.print(packet2[i]+ " ");
			}
			System.out.print("\n");
		}catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		sendSocket.close(); //closes sockets
		receiveSocket.close(); //closes sockets
		sendReceiveSocket.close(); //closes sockets
	}
		
	/**
	 * The main method for the IntermediateHost class that will repeat the Host's tasks forever so long as there are no exceptions or other errors.
	 * @param args array of type String
	 * @throws IOException If there is an issue with I/O.
	 * @throws InterruptedException If there is an issue from sleep.
	 */
		public static void main( String args[]) throws IOException, InterruptedException {
	   	  while(true) {
	   		  IntermediateHost host = new IntermediateHost();
	   		  host.sendAndReceive();
	   	}  
	}	
}








	

