# Simulador de procesos y planificacion de procesos con RR

_Este es un proyecto para el curso "Sistemas Operativos" del cuarto año de la carrera de ingeniería en informática y sistemas, de la Universidad Rafael Landívar campus Quetzaltenango, el cual se orienta en la simulación de cómo el sistema operativo administra los distintos procesos que se le son enviados por medio de una planificación de procesos Round Robin o RR con el cual utilizamos un Quantum de 3 segundos para poder visualizar el trabajo del procesador_


## Comenzando 🚀
Al iniciar el proyecto automáticamente se ejecutará un aventaba principal la cuál abarca el proyecto completo, por forma predeterminada se agregan 10 procesos en la entrada del proceso, estos generados con un tiempo de llegada y un tiempo de consumo automáticamente, con el objetivo de poder simular como la memoria acopla los procesos según el espacio que tenga disponible en la memoria.

Estos tiempos de llegada y de procesamiento se pueden editar manualmente desde la tabla *antes de presionar el botón "iniciar"* 

Al presionar el botón **iniciar** automáticamente se moverán los procesos a la memoria, siempre y cuando el procesador les encuentre espacio en la memoria
Se puede observar en el área **CPU** cómo estos van siendo procesados dependiente el Quantum de 3 segundos planificado con anterioridad, además del contador de programa para saber en qué parte del por eso está trabajando

También se muestra en las cajas b y H que parte de la memoria es la que está siendo ocupada por el proceso en ese instante



### funcionamiento y clases🔧

_Orientamos el funcionamiento del proyecto a la POO ya que utilizamos clases que generarán o entregarán ciertos atributos que consideramos, de cierta forma propias de cada objeto_

_Por lo que utilizamos una clase *process* la cuál guarda todos los atributos del proceso como tal, tanto nombre del proceso o ID, tiempos de consumo y llegada, estado en el que se encuentra el proceso e información de si estaba planificado en la memoria_

_También generamos la clase *RoundRobin* la cual se enfoca en poder planificar el tiempo que el procesador trabajará con cada proceso, utilizando el Quantum previsto, dando un tiempo de consumo determinado para cada proceso, dejándolos en la memoria únicamente si aún no han sido procesados en su totalidad_

_Se agrego un activador de un segundo para mostrar el cambio entre procesos dentro del RR_

_La clase reloj busca mantenerse por medio de un hilo, actualizando y obteniendo la hora actual del sistema_

_Por último tenemos la clase Main que es un form en el cual se encuentra la parte gráfica de todo el proceso, se utiliza para poder generar los procesos en un inicio, aunque su función principal es poder demostrar la simulación de forma gráfica y compresible_

Se implementaron algunos botones de ***+ y -*** al igual que ***reiniciar*** para poder agregar y quitar procesos dentro de la lsita
_ **+** Agrega procesos a la lista de procesos, se genera con tiempo de llegada y tiempo de consumo aleatorio
_ **-** Quita procesos a la lista de procesos
_ **Reiniciar** vuelve el programa a tiempo 0, para poder ingresar nuevos procesos y tiempos


## Construido con 🛠️

* [JAVA Ant](https://ant.apache.org/) - Manejador de dependencias para el programa en general 

## Autores ✒️

+**Pablo Puac** 
+**Carlos Cancinos** 
+**Jose Az** 
+**Jesús Queme** ?

También puedes mirar la lista de todos los [contribuyentes](https://github.com/ppuacgarcia/ProyectoSO/graphs/contributors) quienes han participado en este proyecto. 


## Agradecimientos 🎁

* Gracias por tomarse el tiempo de ver nuestro proyecto, esperamos pueda ser útil para entender el procesamiento y planificación de procesos RR
