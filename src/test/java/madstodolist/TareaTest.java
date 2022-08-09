package madstodolist;


import madstodolist.model.Tarea;
import madstodolist.model.TareaRepository;
import madstodolist.model.Usuario;
import madstodolist.model.UsuarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@SpringBootTest
@Sql("/datos-test.sql")
@Sql(scripts = "/clean-db.sql", executionPhase = AFTER_TEST_METHOD)
public class TareaTest {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    TareaRepository tareaRepository;

    //
    // Tests modelo Tarea en memoria, sin la conexión con la BD
    //

    @Test
    public void crearTarea() {
        // GIVEN
        // Creado usuario nuevo,

        Usuario usuario = new Usuario("juan.gutierrez@gmail.com");

        // WHEN
        // se crea una nueva tarea con ese usuario,

        Tarea tarea = new Tarea(usuario, "Práctica 1 de MADS");

        // THEN
        // el título y el usuario de la tarea son los correctos.

        assertThat(tarea.getTitulo()).isEqualTo("Práctica 1 de MADS");
        assertThat(tarea.getUsuario()).isEqualTo(usuario);
    }

    @Test
    public void laListaDeTareasDeUnUsuarioSeActualizaEnMemoriaConUnaNuevaTarea() {
        // GIVEN
        // Un usuario nuevo creado en memoria, sin conexión con la BD,

        Usuario usuario = new Usuario("juan.gutierrez@gmail.com");

        // WHEN
        // se crea una tarea de ese usuario,

        Set<Tarea> tareas = usuario.getTareas();
        Tarea tarea = new Tarea(usuario, "Práctica 1 de MADS");

        // THEN
        // la tarea creada se ha añadido a la lista de tareas del usuario.

        assertThat(usuario.getTareas()).contains(tarea);
        assertThat(tareas).contains(tarea);
    }

    @Test
    public void comprobarIgualdadTareasSinId() {
        // GIVEN
        // Creadas tres tareas sin identificador, y dos de ellas con
        // la misma descripción

        Usuario usuario = new Usuario("juan.gutierrez@gmail.com");
        Tarea tarea1 = new Tarea(usuario, "Práctica 1 de MADS");
        Tarea tarea2 = new Tarea(usuario, "Práctica 1 de MADS");
        Tarea tarea3 = new Tarea(usuario, "Pagar el alquiler");

        // THEN
        // son iguales (Equal) las tareas que tienen la misma descripción.

        assertThat(tarea1).isEqualTo(tarea2);
        assertThat(tarea1).isNotEqualTo(tarea3);
    }

    @Test
    public void comprobarIgualdadTareasConId() {
        // GIVEN
        // Creadas tres tareas con distintas descripciones y dos de ellas
        // con el mismo identificador,

        Usuario usuario = new Usuario("juan.gutierrez@gmail.com");
        Tarea tarea1 = new Tarea(usuario, "Práctica 1 de MADS");
        Tarea tarea2 = new Tarea(usuario, "Lavar la ropa");
        Tarea tarea3 = new Tarea(usuario, "Pagar el alquiler");
        tarea1.setId(1L);
        tarea2.setId(2L);
        tarea3.setId(1L);

        // THEN
        // son iguales (Equal) las tareas que tienen el mismo identificador.

        assertThat(tarea1).isEqualTo(tarea3);
        assertThat(tarea1).isNotEqualTo(tarea2);
    }

    //
    // Tests TareaRepository
    //

    @Test
    public void guardarTareaEnBaseDatos() {
        // GIVEN
        // Cargados datos de prueba del fichero datos-test.sql,

        Usuario usuario = usuarioRepository.findById(1L).orElse(null);
        Tarea tarea = new Tarea(usuario, "Práctica 1 de MADS");

        // WHEN
        // salvamos la tarea en la BD,

        tareaRepository.save(tarea);

        // THEN
        // se actualiza el id de la tarea,

        assertThat(tarea.getId()).isNotNull();

        // y con ese identificador se recupera de la base de datos la tarea
        // con los valores correctos de las propiedades y la relación con
        // el usuario actualizado también correctamente (la relación entre tarea
        // y usuario es EAGER).

        Tarea tareaBD = tareaRepository.findById(tarea.getId()).orElse(null);
        assertThat(tareaBD.getTitulo()).isEqualTo(tarea.getTitulo());
        assertThat(tareaBD.getUsuario()).isEqualTo(usuario);
    }

    @Test
    public void salvarTareaEnBaseDatosConUsuarioNoBDLanzaExcepcion() {
        // GIVEN
        // Creamos un usuario nuevo que no añadimos a la BD y creamos
        // una tarea asociada a ese usuario,

        Usuario usuario = new Usuario("juan.gutierrez@gmail.com");
        Tarea tarea = new Tarea(usuario, "Práctica 1 de MADS");

        // WHEN // THEN
        // se lanza una excepción al intentar salvar la tarea en la BD

        Assertions.assertThrows(Exception.class, () -> {
            tareaRepository.save(tarea);
        });
    }

    @Test
    public void unUsuarioTieneUnaListaDeTareas() {
        // GIVEN
        // Cargados datos de prueba del fichero datos-test.sql,

        // WHEN
        // recuperamos un ususario de la base de datos,
        Usuario usuario = usuarioRepository.findById(1L).orElse(null);

        // THEN
        // su lista de tareas también se recupera, porque se ha
        // definido la relación de usuario y tareas como EAGER.

        assertThat(usuario.getTareas()).hasSize(2);
    }

    @Test
    public void añadirUnaTareaAUnUsuarioEnBD() {
        // GIVEN
        // Cargados datos de prueba del fichero datos-test.sql,

        // WHEN
        // creamos una nueva tarea con un usuario recuperado de la BD
        // y la salvamos,

        Usuario usuario = usuarioRepository.findById(1L).orElse(null);
        Tarea tarea = new Tarea(usuario, "Práctica 1 de MADS");
        tareaRepository.save(tarea);
        Long tareaId = tarea.getId();

        // THEN
        // la tarea queda guardada en la BD asociada al usuario.

        Usuario usuarioBD = usuarioRepository.findById(1L).orElse(null);
        Tarea tareaBD = tareaRepository.findById(tareaId).orElse(null);
        assertThat(tareaBD).isEqualTo(tarea);
        assertThat(usuarioBD.getTareas()).contains(tareaBD);
        assertThat(tarea.getUsuario()).isEqualTo(usuarioBD);
    }

    //
    // Tests modelo Tarea con la conexión con la BD abierta usando la
    // anotación @Transactional y el TareaRepository
    //

    @Test
    // Al usar @Transactional se ejecuta el test con
    // la conexión con la BD abierta y las entidades conectadas
    // con la base de datos
    @Transactional
    public void cambioEnLaEntidadEnTransactionalModificaLaBD() {
        // GIVEN
        // Cargados datos de prueba del fichero datos-test.sql, habiendo
        // anotado el test con @Transactional y recuperada una tarea,

        Tarea tarea = tareaRepository.findById(1L).orElse(null);

        // WHEN
        // modificamos la descripción de la tarea

        tarea.setTitulo("Esto es una prueba");

        // THEN
        // la descripción queda actualizada en la BD.

        Tarea tareaBD = tareaRepository.findById(1L).orElse(null);
        assertThat(tareaBD.getTitulo()).isEqualTo(tarea.getTitulo());
    }
}
