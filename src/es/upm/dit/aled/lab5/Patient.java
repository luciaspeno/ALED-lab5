package es.upm.dit.aled.lab5;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import es.upm.dit.aled.lab5.gui.EmergencyRoomGUI;
import es.upm.dit.aled.lab5.gui.Position2D;

/**
 * Models a patient in a hospital ER. Each Patient is characterized by its
 * number (which must be unique), its current location and a protocol. The
 * protocol is a List of Transfers. Each Patient also has an index to indicate
 * at which point of the protocol they are at the current time.
 * 
 * Patients are Threads, and therefore must implement the run() method.
 * 
 * Each Patient is represented graphically by a dot of diameter 10 px, centered
 * in a given position and with a custom color.
 * 
 * @author rgarciacarmona
 */
public class Patient extends Thread {

	private int number;
	private List<Transfer> protocol;
	private int indexProtocol;
	private Area location;
	private Position2D position;
	private Color color;

	/**
	 * Builds a new Patient.
	 * 
	 * @param numbre          The number of the Patient.
	 * @param initialLocation The initial location of the Patient.
	 */
	public Patient(int number, Area initialLocation) {
		this.number = number;
		this.protocol = new ArrayList<Transfer>();
		this.indexProtocol = 0;
		this.location = initialLocation;
		this.position = initialLocation.getPosition();
		this.color = Color.GRAY; // Default color
	}

	/**
	 * Returns the number of the Patient.
	 * 
	 * @return The number.
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * Returns the protocol of the Patient.
	 * 
	 * @return The protocol.
	 */
	public List<Transfer> getProtocol() {
		return protocol;
	}

	/**
	 * Returns the current location of the Patient.
	 * 
	 * @return The current location.
	 */
	public Area getLocation() {
		return location;
	}

	/**
	 * Changes the current location of the Patient.
	 * 
	 * @param location The new location.
	 */
	public void setLocation(Area location) {
		this.location = location;
	}

	/**
	 * Returns the position of the Patient in the GUI.
	 * 
	 * @return The position.
	 */
	public Position2D getPosition() {
		return position;
	}

	/**
	 * Changes the position of the Patient in the GUI.
	 * 
	 * @param position The new position.
	 */
	public void setPosition(Position2D position) {
		this.position = position;
	}

	/**
	 * Returns the color of Patient in the GUI.
	 * 
	 * @return The color.
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Changes the color of the Patient in the GUI.
	 * 
	 * @param color The new color.
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Adds a new Transfer at the end of the Patient's protocol.
	 * 
	 * @param transfer The new Transfer.
	 */
	public void addToProtocol(Transfer transfer) {
		this.protocol.add(transfer);
	}

	/**
	 * Advances the Patient's protocol. The Patient is moved to the new Area, the
	 * movement is animated by the GUI and the index is increased by one.
	 */
	private void advanceProtocol() {
		Transfer transfer = protocol.get(indexProtocol);
		indexProtocol++;
		System.out.println("Patient " + this.number + " is moving from " + this.location + " to " + transfer.getTo());
		EmergencyRoomGUI.getInstance().animateTransfer(this, transfer);
		this.location = transfer.getTo();
	}

	/**
	 * Simulates the treatment of the Patient at its current location. Therefore,
	 * the Patient must spend at this method the amount of time specified in such
	 * Area.
	 */
	private void attendedAtLocation() {
		try {
			System.out.println("Patient " + this.number + " is being attended at " + this.location);
			sleep(this.location.getTime());
		} catch (InterruptedException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt(); // Restore interrupted status
		}
	}

	/**
	 * Executes the Patient's behavior. It follows their protocol by being attended
	 * at the current location and then moving onto the next, until the last step of
	 * the protocol is reached. At that point, the Patient is removed from the GUI.
	 * 
	 * It is important to note that: - Before being attended at a location, the
	 * Patient must first enter it. If it's full, they will be kept waiting. - After
	 * being attended at a location, the Patient must leave it. By doing this, the
	 * Area knows that it must allow access to another Patient that was waiting.
	 */
	
//	cada paciente debe:
//
//		1. Intentar entrar en el área actual usando enter()
//
//		2. Ser atendido (sleep(time))
//
//		3. Salir usando exit()
//
//		4. Moverse al siguiente area con Transfer (advanceProtocol())
//
//		5. Repetir hasta terminar su recorrido
	
	@Override
	public void run() {
		// TODO

			
			do {
			    // Área actual en la que está el paciente: this.location;

		        // 1) Intentar acceder a la ubicación actual (enter)
		        this.location.enter(this);

		        // 2) Ser atendido en la ubicación actual
		        //    attendedAtLocation() "bloquea" al paciente el tiempo de ese area
		        //    el tiempo necesario (sleep(time)).
		        this.attendedAtLocation();

		        // 3) Salir de la ubicación actual (exit)
		        this.location.exit(this);

		        // 4) Avanzar al siguiente paso del protocolo
		        this.advanceProtocol();

		        // 5) Repetir proceso mientras queden pasos en el protocolo
		        
			}while(indexProtocol<protocol.size());//Repetir el proceso hasta acabar la lista de tareas
			
			 // Cuando salimos del bucle, hemos hecho todos los Transfers,
		    // y la location ahora es el ÚLTIMO área del recorrido del protocolo
		    // Aún falta que el paciente sea atendido en esta última área:
			
			this.location.enter(this);
			this.attendedAtLocation();	
			this.location.exit(this);

			 
			EmergencyRoomGUI.getInstance().removePatient(this);
			
			//Trazas
			System.out.println ("El paciente: "+this.number+" ha terminado de ser atendido en "+ this.location.getName());
			
		
		}
	

	}

