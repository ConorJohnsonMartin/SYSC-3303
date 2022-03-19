package Assignment4;

import java.io.IOException;
import java.net.*;


/**
 * A class that represents a server that receives the clients message and responds if the packet contained a valid read / write request.
 * 
 * @author Conor Johnson Martin 101106217
 * @version March 5th, 2022
 */
public class Server {

	private DatagramSocket receiveSocket, sendSocket, receiveACK;
	private DatagramPacket sendPacket, receivePacket, ackPacket;
	private byte[] temp = new byte[4];
	private String requestPacket = "RequestPacket";

	/**
	 * Default constructor for the Server class that initializes sockets
	 */
	public Server()  {
		try {
			sendSocket = new DatagramSocket();
			receiveSocket = new DatagramSocket(69);
			receiveACK = new DatagramSocket(501);
		}catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}

	}

	/**
	 * A method that will receive a packet sent from the IntermediateHost and will respond by sending back a packet under certain circumstances (i.e. if the parser determines that the packet is a valid read / write)
	 * @throws Exception if the packet is not a valid read / write (i.e. does not start with 0, 1 or 0, 2)
	 */
	public void sendAndReceive() throws Exception {
		Thread.sleep(8);		

		//Send REQ packet to IntermediateHost
		byte[] req = new byte[requestPacket.getBytes().length];
		System.arraycopy(requestPacket.getBytes(), 0, req, 0, requestPacket.getBytes().length);
		sendPacket = new DatagramPacket(req, req.length, InetAddress.getLocalHost(), 24);;
		try {
			//Thread.sleep(5);
			sendSocket.send(sendPacket);
			//System.out.print("Server - [REQ] Sent Host Request Packet (Bytes): ");
			//printBytes(req);
			//System.out.print("Server - [REQ] Sent Host Request Packet (String): " + (new String(req)) + " Timestamp: " + Instant.now());
		}catch(SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}

		//Receive Packet from Host
		byte[] packet = new byte[1000];
		try {
			receivePacket = new DatagramPacket(packet, packet.length);
			//System.out.print("\n" + "Server - waiting for host packet...");
			receiveSocket.receive(receivePacket);
			//Thread.sleep(5);
			//System.out.print("\n" + "Server - [REC] Received packet containing: " + packet.length + " bytes" + " Timestamp: " + Instant.now());
		}catch(SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}

		//Send Parsed packet to IntermediateHost
		byte[] test = new byte[4];
		test = parsePacket(packet);
		try {
			sendPacket = new DatagramPacket(test, test.length, receivePacket.getAddress(), receivePacket.getPort());;
			//Thread.sleep(5);
			sendSocket.send(sendPacket);
			//System.out.print("\n" + "Server - [SEND] Sending a packet: ");
			//printBytes(test);
			//System.out.print(" Timestamp: " + Instant.now());
			//System.out.print("\n");
		}catch(SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}

		//Receive ACK packet to IntermediateHost
		byte[] ack = new byte[15];
		ackPacket = new DatagramPacket(ack, ack.length);
		try {
			receiveACK.receive(ackPacket);
			//Thread.sleep(5);
			//System.out.print("Server - [ACK] Packet received by host: (Bytes) ");
			//printBytes(ack);
			//System.out.println("\n" + "Server - [ACK] Packet received by host (String): " + (new String(ack)) + " Timestamp: " + Instant.now());
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		//System.out.println("-------------------------------------------------------------------------------------------------------------");

		//closes sockets
		sendSocket.close();
		receiveSocket.close();
		receiveACK.close();
	}

	/**
	 * A method that parses an array of bytes to determine if a packet is valid.
	 * @param packet is a DatagramPacket that has been sent from the IntermediateHost
	 * @return an array of byte[] with the corresponding codes (0, 3, 0, 1) for a valid read packet or (0, 4, 0, 0) for a valid write packet.
	 * @throws Exception if the packet is not a valid read / write (i.e. does not start with 0, 1 or 0, 2)
	 */
	public byte[] parsePacket(byte[] packet) throws Exception {
		byte[] read = new byte[] {0, 1};
		byte[] write = new byte[] {0, 2};		
		if(packet[0] == read[0]) {
			if(packet[1] == read[1]) {
				temp = new byte[] {0, 3, 0, 1};
			}
		}
		if(packet[0] == write[0]) {
			if(packet[1] == write[1]) {
				temp = new byte[] {0, 4, 0, 0};
			}
		}else{
			//System.out.println("INVALID PACKET DETECTED");
			throw new Exception("Invalid Packet Exception");
		}
		return temp;
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
	 * The main method for the Server class that will repeat the Server's tasks forever unless an invalid packet is received or another error occurs.
	 * @param args array of type String
	 * @throws Exception if the packet is not a valid read / write (i.e. does not start with 0, 1 or 0, 2)
	 */
	public static void main( String args[]) throws Exception {
		while(true) {
			Server server= new Server();
			server.sendAndReceive();
		}
	}
}