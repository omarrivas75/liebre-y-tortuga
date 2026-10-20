package com.mycompany.liebreytortuga;


import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;


import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;


public class LytorController implements Initializable {


@FXML
private Button btnIniciar;

@FXML
private Label lblLiebre;


@FXML
private ProgressBar progressLiebre;


@FXML
private Label lblTortuga;


@FXML
private ProgressBar progressTortuga;


private final Random azar = new Random();


private final AtomicBoolean carreraTerminada =
new AtomicBoolean(false);


@Override
public void initialize(URL url, ResourceBundle rb) {
progressLiebre.setProgress(0);
progressTortuga.setProgress(0);
}


@FXML
private void iniciarCarrera() {


 btnIniciar.setDisable(true);


progressLiebre.progressProperty().unbind();
progressTortuga.progressProperty().unbind();


progressLiebre.setProgress(0);
progressTortuga.setProgress(0);


carreraTerminada.set(false);


lblLiebre.setText("La Liebre: ¡Está corriendo!");
lblTortuga.setText("La Tortuga: ¡Está corriendo!");


final int velocidadLiebre = 40 + azar.nextInt(101);
final int velocidadTortuga = 70 + azar.nextInt(101);


final double puntoDormir =
(20 + azar.nextInt(51)) / 100.0;


final int tiempoDormir =
1000 + azar.nextInt(4001);


Task<Void> tareaLiebre = new Task<>() {
@Override
protected Void call() throws Exception {


for (int i = 1; i <= 100; i++) {


 if (carreraTerminada.get()) {
 return null;
}
Thread.sleep(velocidadLiebre);
 updateProgress(i, 100);

if (i == (int) (puntoDormir * 100)) {
  updateMessage("dormida");


 Thread.sleep(tiempoDormir);

 if (carreraTerminada.get()) {
return null;
  }
  updateMessage("corriendo");
 }
 }
 return null;
 }
   };
Task<Void> tareaTortuga = new Task<>() {
@Override
  protected Void call() throws Exception {
  for (int i = 1; i <= 100; i++) {
  if (carreraTerminada.get()) {
                        return null;
 }
  Thread.sleep(velocidadTortuga);
  updateProgress(i, 100);
 }
     return null;
            }
        };
 progressLiebre.progressProperty().bind(
                tareaLiebre.progressProperty()
        );
 progressTortuga.progressProperty().bind(
                tareaTortuga.progressProperty()
);

 tareaLiebre.messageProperty().addListener(
   (obs, anterior, mensaje) -> {
 if ("dormida".equals(mensaje)) {
 lblLiebre.setText(
 "La Liebre: ¡Se durmió!"
                        );
 } else if ("corriendo".equals(mensaje)) {
lblLiebre.setText(
  "La Liebre: ¡Despertó y corre!"
    );
  }
 }
);

        tareaLiebre.setOnSucceeded(e -> {
 if (carreraTerminada.compareAndSet(false, true)) {
  progressLiebre.setProgress(1.0);
 lblLiebre.setText(
                        "La Liebre: ¡Llegó a la meta!"
  );
 lblTortuga.setText(
 "La Tortuga: ¡La liebre llegó primero!"
  );
  btnIniciar.setDisable(false);
  }
 });

 tareaTortuga.setOnSucceeded(e -> {
   if (carreraTerminada.compareAndSet(false, true)) {
     progressTortuga.setProgress(1.0);
     lblTortuga.setText(
              "La Tortuga: ¡Llegó a la meta!"
    );
        lblLiebre.setText(
      "La Liebre: ¡Llegó demasiado tarde!"
       );
    btnIniciar.setDisable(false);
    }
    });

tareaLiebre.setOnFailed(e -> {
     if (carreraTerminada.compareAndSet(false, true)) {
       btnIniciar.setDisable(false);
    lblLiebre.setText("Error en la carrera.");
     }
        });
        tareaTortuga.setOnFailed(e -> {
   if (carreraTerminada.compareAndSet(false, true)) {
      btnIniciar.setDisable(false);
    lblTortuga.setText("Error en la carrera.");
    }
 });

 Thread hiloLiebre = new Thread(tareaLiebre);
 Thread hiloTortuga = new Thread(tareaTortuga);

 hiloLiebre.setDaemon(true);
        hiloTortuga.setDaemon(true);
  hiloLiebre.start();
        hiloTortuga.start();
    }
}