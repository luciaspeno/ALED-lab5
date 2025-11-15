package es.upm.dit.aled.lab5;

import java.util.LinkedList;
import java.util.Queue;

import es.upm.dit.aled.lab5.gui.Position2D;

/**
 * Extension of Area that maintains a strict queue of the Patients waiting to
 * enter in it. After a Patient exits, the first one in the queue will be
 * allowed to enter.
 * 
 * @author rgarciacarmona
 */
//Es una clase HIJA de Area, es decir, tiene acceso a los metodos de Area y usamos super
public class AreaQueue extends Area {
	
	//Quiero que el primero que llegue sea el primero que entre cuando se libera un hueco.
	//Hago una cola de espera y cuando se libera un hueco elimino el primero que es el que entra
 
	// TODO
	//En java QUEUE es una interfaz y se implementa con una LinkedList
	private Queue <Patient> queueDeEspera = new LinkedList<>();; //Creo una cola vacia de pacientes
	
	
//CONSTRUCTOR
	public AreaQueue(String name, int time, int capacity, Position2D position, Queue queueDeEspera) {
	    super(name, time, capacity, position);  // llamamos al constructor de Area
	    this.queueDeEspera = queueDeEspera; //Inicializamos cola 
	}
	
//MÉTODO ENTER
//	Cómo debe funcionar enter() ahora
	//	En AreaQueue, enter(Patient p) sigue esta lógica :
		//	Cuando un paciente llega al área:
		//	Se añade al final de la cola waitQueue.
	
		//	Mientras: NO haya hueco en el área (numPatients >= capacity)	O
				//	NO sea su turno (no es el primero de la cola: waitQueue.peek() != p),
				//	→ el paciente se queda esperando (wait()).
	
		//	Cuando:
		//	hay hueco (numPatients < capacity)	Y	es su turno (waitQueue.peek() == p),
		//	→ entonces:
			//	se saca de la cola (remove()),
			//	entra en el área (numPatients++).
	@Override
	public synchronized void enter(Patient p) {

	    // 1. El paciente llega y se añade al final de la cola
		queueDeEspera.add(p);

	    // 2. Mientras NO sea su turno o NO haya hueco, espera
	    while (queueDeEspera.peek() != p || numPatients >= capacity) {
	        try {
	            wait();
	        } catch (InterruptedException e) {
	            Thread.currentThread().interrupt();
	        }
	    }

	    // 3. Si hemos salido del while:
	    //    - hay hueco
	    //    - y este paciente es el primero de la cola
	    queueDeEspera.remove();  // sale de la cola
	    numPatients++;       // entra en el área (ya está siendo atendido)
	}

}
