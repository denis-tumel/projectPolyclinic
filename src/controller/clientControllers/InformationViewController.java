package controller.clientControllers;

import config.LoaderStage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import model.ObjMessage;
import objects.Doctor;
import objects.User;
import start.StartClient;
import utils.DialogManager;

import java.io.IOException;

public class InformationViewController {
    @FXML
    private Label day;
    @FXML
    private Label speciality;

    private Doctor doctor;
    private User user;
    private ObjMessage objMessage;

    public void ActionAccess(ActionEvent actionEvent) {
        objMessage = createObjMessage(user, doctor);
        try {
            System.out.println( objMessage.getDoctorObject().getDay());
            StartClient.getOutputStream().writeObject(objMessage);
            StartClient.getOutputStream().flush();

            objMessage = (ObjMessage) StartClient.getInputStream().readObject();
            if (objMessage.getOrderTicket().getFlag()) {
                DialogManager.showInfoDialog("инфо", "Заказ успешно оформлен!");
                user.setId(objMessage.getOrderTicket().getUserID());

                LoaderStage.getViewStage().close();
                LoaderStage.getClientController().setInformation(user);
                LoaderStage.getClientStage().show();
            } else {
                DialogManager.showErrorDialog("ошибка", "На этот день талоны закончились, выберите другой день!");

                LoaderStage.getViewStage().close();
                LoaderStage.getOrderStage().show();

            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private ObjMessage createObjMessage(User user, Doctor doctor) {
        objMessage = new ObjMessage();
        doctor.setType(ObjMessage.ORDER_TICKET);
        objMessage.setTypeObject(ObjMessage.DOCTOR);
        objMessage.setUserObject(user);
        objMessage.setDoctorObject(doctor);

        return objMessage;
    }

    public void setInformation(Doctor selectedDoctor, User user) {
        this.doctor = selectedDoctor;
        this.user = user;
        this.day.setText(doctor.getDay());
        speciality.setText(selectedDoctor.getSpecialty());
    }

    public void ActionCancel(ActionEvent actionEvent) {
        LoaderStage.getViewStage().close();
        LoaderStage.getOrderStage().show();
    }
}
