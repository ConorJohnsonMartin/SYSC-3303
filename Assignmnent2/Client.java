package Assignmnent2;
import java.io.*;
import java.net.*;

/**
 * A class that represents a client that builds a packet and sends it to be handled by a server.
 * 
 * @author Conor Johnson Martin 101106217
 * @version February 11th, 2022
 */
public class Client {
	
	private byte[] read;
	private byte[] write;
	private byte[] zero;
	private byte[] wrong;
	private DatagramPacket sendPacket, receivePacket;
	private DatagramSocket sendReceiveSocket;
	private String fileName = "SYSC3303Assignment2.txt";
	private String mode = "octet";
	private byte[] readPacket;
	private byte[] writePacket;
	private byte[] invalidPacket;
	
	/**
	 * Default constructor for the Client class that initializes all of the byte arrays used to build the required read / write packets
	 * Also initializes sockets
	 */
	public Client() {
		try {
			sendReceiveSocket = new DatagramSocket();
			read = new byte[] {0, 1};
			write = new byte[] {0, 2};
			zero = new byte[] {0};
			wrong = new byte[] {9, 9};
			//complicated way of calculating the length of byte[] required but I did it just for fun lol.
			readPacket = new byte[read.length + fileName.getBytes().length + zero.length +  mode.getBytes().length + zero.length];
			writePacket = new byte[write.length + fileName.getBytes().length + zero.length +  mode.getBytes().length + zero.length];
			invalidPacket = new byte[wrong.length + fileName.getBytes().length + zero.length +  mode.getBytes().length + zero.length];
		
		}catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * A method that sends a readPacket when int i is odd, and sends a writePacket when int i is even. If int i == 11, the method will send an invalidPacket to be handled.
	 * This method also receives a response from IntermediateHost if the packet is valid.
	 * @throws InterruptedException If there is an issue with sleep.
	 */
	public void sendAndReceive() throws InterruptedException {
		byte[] packet = new byte[32];
		for(int i = 1; i <= 11; i++) {
			if(i == 11) {
				packet = buildInvalidPacket();
			}
			else if(i % 2 == 1) {
				packet = buildReadPacket();
			}else {
				packet = buildWritePacket();
			}
			
			//Send packet to IntermediateHost
			System.out.print("Client - Send Packet (Bytes): ");
	        for(int j = 0; j < packet.length; j++) {
	              System.out.print(packet[j] + " ");
	          }
	        System.out.println("\n" + "Client - Send Packet (String): " + (new String(packet)));
			
			try {
		         sendPacket = new DatagramPacket(packet, packet.length, InetAddress.getLocalHost(), 23);	         
		      } catch (UnknownHostException e) {
		         e.printStackTrace();
		         System.exit(1);
		      }
			try {
				Thread.sleep(5);
                sendReceiveSocket.send(sendPacket);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
			
			//Receive packet from IntermediateHost
			byte[] packet1 = new byte[4];
			receivePacket = new DatagramPacket(packet1, packet1.length);
			try {
				Thread.sleep(5);
				 sendReceiveSocket.receive(receivePacket);
	            } catch (IOException e) {
	                e.printStackTrace();
	                System.exit(1);
	            }
			 System.out.println("Client - Packet received from host: " + receivePacket.getAddress());

	         System.out.print("Client - Packet received as bytes: ");
	         for(int j = 0; j < packet1.length; j++) {
                 System.out.print(packet1[j]+ " ");
             }
	         System.out.print("\n");
		}
		sendReceiveSocket.close(); //closes sockets
		
	}
	
	/**
	 * Builds the readPacket (starts with 0, 1) using separate byte arrays (again just for fun hahaha).
	 * @return the readPacket
	 */
	public byte[] buildReadPacket(){
		System.arraycopy(read, 0, readPacket, 0, read.length);
		System.arraycopy(fileName.getBytes(), 0, readPacket, read.length, fileName.getBytes().length);
		System.arraycopy(zero, 0, readPacket, read.length + fileName.getBytes().length, zero.length);
		System.arraycopy(mode.getBytes(), 0, readPacket, read.length + fileName.getBytes().length + zero.length , mode.getBytes().length);
		System.arraycopy(zero, 0, readPacket, read.length + fileName.getBytes().length + zero.length + mode.getBytes().length, zero.length);        
		return readPacket;
	}
	
	/**
	 * Builds the writePacket (starts with 0, 2) using separate byte arrays
	 * @return the writePacket
	 */
	public byte[] buildWritePacket(){
		System.arraycopy(write, 0, writePacket, 0, write.length);
		System.arraycopy(fileName.getBytes(), 0, writePacket, write.length, fileName.getBytes().length);
		System.arraycopy(zero, 0, writePacket, write.length + fileName.getBytes().length, zero.length);
		System.arraycopy(mode.getBytes(), 0, writePacket, write.length + fileName.getBytes().length + zero.length , mode.getBytes().length);
		System.arraycopy(zero, 0, writePacket, write.length + fileName.getBytes().length + zero.length + mode.getBytes().length, zero.length);
		return writePacket;
	}
	
	/**
	 * Builds the invalidPacket (starts with anything but 0, 1 or 0, 2 (in this case I chose 9, 9)) using separate byte arrays
	 * @return the invalidPacket
	 */
	public byte[] buildInvalidPacket(){
		System.arraycopy(wrong, 0, invalidPacket, 0, wrong.length);
		System.arraycopy(fileName.getBytes(), 0, invalidPacket, wrong.length, fileName.getBytes().length);
		System.arraycopy(zero, 0, invalidPacket, wrong.length + fileName.getBytes().length, zero.length);
		System.arraycopy(mode.getBytes(), 0, invalidPacket, wrong.length + fileName.getBytes().length + zero.length , mode.getBytes().length);
		System.arraycopy(zero, 0, invalidPacket, wrong.length + fileName.getBytes().length + zero.length + mode.getBytes().length, zero.length);      
		return invalidPacket;
	}

	/**
	 * The main method of the Client class
	 * @param args array of type String
	 * @throws InterruptedException If there is an issue from sleep.
	 */
	public static void main(String args[]) throws InterruptedException {
	      Client c = new Client();
	      c.sendAndReceive();      
	}
}
	
	