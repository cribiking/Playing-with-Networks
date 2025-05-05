Xarxes  —  Curs 2024-2025 

Grau en Enginyeria Informàtica 

===============================
# Chatín 

**Chatín** es una sencilla aplicación de chat basada en una arquitectura cliente-servidor. Permite que dos usuarios se comuniquen en tiempo real desde dos terminales distintos.

## Funcionamiento general

- El **servidor** espera una única conexión en un puerto específico.
- El **cliente** intenta conectarse al servidor.  
  - Si no lo consigue (porque no está disponible o ya hay una conexión activa), muestra el mensaje `"Servidor no disponible."` y finaliza.
- Cuando la conexión se establece con éxito, el servidor confirma la conexión enviando el mensaje: `"Connexió acceptada."`

## Comunicación entre cliente y servidor

- Ambos pueden **enviar y recibir mensajes al mismo tiempo**.
- No es necesario turnarse para hablar, gracias al uso de **hilos de ejecución (threads)**.
- Cada mensaje recibido se muestra en pantalla indicando quién lo envió.  
  - Ejemplo: `Client: «Hola»`
- No se permiten mensajes vacíos.

## Finalización de la conversación

- La conversación termina cuando cualquiera de los dos escribe y envía el mensaje `"FI"`.
- También puede finalizar si se interrumpe la conexión (por ejemplo, con `Ctrl+C`).
- El programa está preparado para **cerrarse correctamente** en cualquier situación.

---

Este proyecto es una introducción práctica al uso de **sockets**, **hilos** y la gestión de entradas/salidas en aplicaciones de red.
