package Assignmnent2;
import java.net.*;

/**
 * A class that represents a server that receives the clients message and responds if the packet contained a valid read / write request.
 * 
 * @author Conor Johnson Martin 101106217
 * @version February 11th, 2022
 */
public class Server {

	private DatagramSocket receiveSocket, sendSocket;
	private DatagramPacket sendPacket, receivePacket;
	private byte[] temp = new byte[4];
	
	/**
	 * Default constructor for the Server class that initializes sockets
	 */
	public Server()  {
		try {
			sendSocket = new DatagramSocket();
			receiveSocket = new DatagramSocket(69);
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
		byte[] packet = new byte[32];
		
		receivePacket = new DatagramPacket(packet, packet.length);
		try {
			System.out.println("Server waiting for packet...");
			receiveSocket.receive(receivePacket);
			Thread.sleep(5);
			
			System.out.print("Server - Received packet (Bytes): ");
			for(int i = 0; i < packet.length; i++) {
				System.out.print(packet[i]+ " ");
			}
			System.out.println("\n" + "Server - Received packet (String): " + (new String(packet)));
		}catch(SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
		
		byte[] test = new byte[4];
		test = parsePacket(packet);
		
		sendPacket = new DatagramPacket(test, test.length, receivePacket.getAddress(), receivePacket.getPort());;
		try {
			Thread.sleep(5);
			sendSocket.send(sendPacket);
			
			System.out.print("Server - Sending a packet: ");
			for(int i = 0; i < test.length; i++) {
				System.out.print(test[i]+ " ");
			}
			System.out.print("\n");
		}catch(SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
		
		sendSocket.close(); //closes sockets
		receiveSocket.close(); //closes sockets
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
			System.out.println("INVALID PACKET DETECTED");
			throw new Exception("Invalid Packet Exception");
		}
		return temp;
		
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

