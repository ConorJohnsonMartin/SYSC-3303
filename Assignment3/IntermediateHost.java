package Assignment3;

import java.io.*;
import java.net.*;

/**
 * A class that represents an intermediate connection between the client and server.
 * 
 * @author Conor Johnson Martin 101106217
 * @version March 5th, 2022
 */
public class IntermediateHost {

	private DatagramSocket receiveClientSocket, receiveServerSocket, sendSocket, sendReceiveSocket; 
	private DatagramPacket clientPacket, serverPacket, responsePacket, clientResponsePacket, clientACK, serverACK;
	private String acknowledgement = "MessageReceived";


	/**
	 * Default constructor for the IntermediateHost class that initializes sockets
	 */
	public IntermediateHost() {
		try {
			receiveClientSocket = new DatagramSocket(23);
			receiveServerSocket = new DatagramSocket(24);
			sendSocket = new DatagramSocket();
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
		try {
			System.out.println("Host - waiting for client packet...");
			clientPacket = new DatagramPacket(packet1, packet1.length);
			receiveClientSocket.receive(clientPacket);
			Thread.sleep(5);
			System.out.print("Host - [REC] Received Packet from Client (Bytes): ");
			printBytes(packet1);
			System.out.println("\n" + "Host - [REC] Received Packet from Client (String): " + (new String(packet1)));		
		}catch(SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}

		//Send ACK back to Client
		byte[] ack = new byte[acknowledgement.getBytes().length];
		System.arraycopy(acknowledgement.getBytes(), 0, ack, 0, acknowledgement.getBytes().length);
		try {
			clientACK = new DatagramPacket(ack, ack.length, InetAddress.getLocalHost(), 500);
			Thread.sleep(5);
			sendSocket.send(clientACK);
			System.out.print("Host - [ACK] Send Acknowledgement Packet to Client (Bytes): ");
			printBytes(ack);
			System.out.print("\n" + "Host - [ACK] Send Acknowledgement Packet to Client (String): " + (new String(ack)));
		}catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		//Receive REQ packet from Server
		byte[] req = new byte[15];
		try {
			serverPacket = new DatagramPacket(req, req.length);
			receiveServerSocket.receive(serverPacket);
			Thread.sleep(5);
			System.out.print("\n" + "Host - [REQ] Received Server Request packet (Bytes): ");
			printBytes(req);
			System.out.print("\n" + "Host - [REQ] Received Server Request packet (String): " + (new String(req)));
		}catch(SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}		

		//Forward packet to Server
		try {
			responsePacket = new DatagramPacket(packet1, packet1.length, InetAddress.getLocalHost(), 69);
			Thread.sleep(5);
			sendReceiveSocket.send(responsePacket);
			System.out.print("\n" + "Host - [SEND] Send Packet to Server (Bytes): ");
			printBytes(packet1);
			System.out.println("\n" + "Host - [SEND] Send Packet to Server (String): " + (new String(packet1)));
		}catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		//Receive packet from Server
		byte[] packet2 = new byte[4];
		try {
			serverPacket = new DatagramPacket(packet2, packet2.length);
			sendReceiveSocket.receive(serverPacket);
			Thread.sleep(5);
			System.out.print("Host - [REC] Received Server packet (Bytes): ");
			printBytes(packet2);
			System.out.print("\n");
		}catch(SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}

		//Send ACK to Server
		try {
			serverACK = new DatagramPacket(ack, ack.length, InetAddress.getLocalHost(), 501);
			Thread.sleep(5);
			sendSocket.send(serverACK);
			System.out.print("Host - [ACK] Send Acknowledgement Packet to Server (Bytes): ");
			printBytes(ack);
			System.out.print("\n" + "Host - [ACK] Send Acknowledgement Packet to Server (String): " + (new String(ack)));
		}catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		//Receive REQ packet from client
		byte[] req1 = new byte[15];
		try {
			clientPacket = new DatagramPacket(req1, req1.length);
			Thread.sleep(5);
			receiveClientSocket.receive(clientPacket);
			System.out.print("\n" + "Host - [REQ] Received Client Request packet (Bytes): ");
			printBytes(req1);
			System.out.println("\n" + "Host - [REQ] Received Client Request packet (String): " + (new String(req1)));		
		}catch(SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}		

		//Forward packet to Client
		clientResponsePacket = new DatagramPacket(packet2, packet2.length, clientPacket.getAddress(), clientPacket.getPort());
		try {
			Thread.sleep(5);
			sendSocket.send(clientResponsePacket);
			System.out.print("Host - [SEND] Send Packet to Client (Bytes): ");
			printBytes(packet2);
			System.out.print("\n" + "---------------------------------------------------------------------------------------------------------" + "\n");
		}catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		//closes sockets
		sendSocket.close();
		receiveClientSocket.close();
		sendReceiveSocket.close();
		receiveServerSocket.close();
	}

	/**
	 * A method that prints the bytes from a byte[]
	 * @param bytes is a byte array to be printed
	 */
	public void printBytes(byte[] bytes) {
		for(int j = 0; j < bytes.length; j++) {
			System.out.print(bytes[j] + " ");
		}
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
