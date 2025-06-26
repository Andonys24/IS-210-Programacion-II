package serializacion;

import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {
        var personas = new ArrayList<Object>();
        List<Object> personasDes = null;
        var persona1 = new Persona("Juan", 30);

        personas.add(persona1);
        personas.add(new Persona("Maria", 20));
        personas.add(new Persona("Luis", 40));
        personas.add(new Persona("Pablo", 35));

        // Serializar un solo obj
        Serializer.serializar(persona1, "persona1");

        // Deserializar un solo obj
        Persona personaDes1 = (Persona) Serializer.deserializar("persona1");
        System.out.println(personaDes1);

        // Serializar multiples obj
        Serializer.serializacionMultiple(personas, "Personas");

        // Deserializar multiples obj
        personasDes = Serializer.deserializacionMultiple("Personas");

        for (Object obj : personasDes) {
            System.out.println(obj);
        }

    }
}
