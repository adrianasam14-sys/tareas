package com.ejemplo.tareas;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/tareas")
public class TareaController {

    // Atributos: viven mientras la aplicación esté encendida
    private List<Tarea> tareas = new ArrayList<>();
    private int siguienteId = 1;

    // Constructor: carga los datos iniciales
    public TareaController() {
        tareas.add(new Tarea(siguienteId++, "Aprender Java", false));
        tareas.add(new Tarea(siguienteId++, "Practicar Spring Boot", false));
        tareas.add(new Tarea(siguienteId++, "Subir el proyecto a GitHub", true));
    }

    // Método auxiliar: busca una tarea por id (devuelve null si no existe)
    private Tarea buscarPorId(int id) {
        for (Tarea t : tareas) {
            if (t.getId() == id) {
                return t;
            }
        }
        return null;
    }

    // GET /tareas : listar todas
    @GetMapping
    public List<Tarea> listar() {
        return tareas;
    }

    // GET /tareas/{id} : obtener una (200 o 404)
    @GetMapping("/{id}")
    public ResponseEntity<Tarea> obtener(@PathVariable int id) {
        Tarea tarea = buscarPorId(id);
        if (tarea == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tarea);
    }

    // POST /tareas : crear (201)
    @PostMapping
    public ResponseEntity<Tarea> crear(@RequestBody Tarea nueva) {
        nueva.setId(siguienteId++);
        tareas.add(nueva);
        return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
    }

    // PUT /tareas/{id} : actualizar (200 o 404)
    @PutMapping("/{id}")
    public ResponseEntity<Tarea> actualizar(@PathVariable int id, @RequestBody Tarea datos) {
        Tarea tarea = buscarPorId(id);
        if (tarea == null) {
            return ResponseEntity.notFound().build();
        }
        tarea.setTitulo(datos.getTitulo());
        tarea.setCompletada(datos.isCompletada());
        return ResponseEntity.ok(tarea);
    }

    // DELETE /tareas/{id} : eliminar (204 o 404)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        Tarea tarea = buscarPorId(id);
        if (tarea == null) {
            return ResponseEntity.notFound().build();
        }
        tareas.remove(tarea);
        return ResponseEntity.noContent().build();
    }

    

    
    @GetMapping("/pendientes")
    public List<Tarea> pendientes() {
        List<Tarea> resultado = new ArrayList<>();
        for (Tarea t : tareas) {
            if (!t.isCompletada()) {
                resultado.add(t);
            }
        }
        return resultado;
    }

   
    @PutMapping("/{id}/completar")
    public ResponseEntity<Tarea> completar(@PathVariable int id) {
        Tarea tarea = buscarPorId(id);
        if (tarea == null) {
            return ResponseEntity.notFound().build();
        }
        tarea.setCompletada(true);
        return ResponseEntity.ok(tarea);
    }

    // Ejercicio 3: GET /tareas/buscar/{texto}
    @GetMapping("/buscar/{texto}")
    public List<Tarea> buscar(@PathVariable String texto) {
        List<Tarea> resultado = new ArrayList<>();
        for (Tarea t : tareas) {
            if (t.getTitulo() != null
                    && t.getTitulo().toLowerCase().contains(texto.toLowerCase())) {
                resultado.add(t);
            }
        }
        return resultado;
    }
}