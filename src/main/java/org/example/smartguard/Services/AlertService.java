package org.example.smartguard.Services;
import org.example.smartguard.Model.DataType;
import org.example.smartguard.Model.SensorData;
import org.example.smartguard.Model.User;
import org.springframework.stereotype.Service;

@Service
public class AlertService {
    public void checkAlerts(SensorData data) {
        // Prag critic: Ritm cardiac > 100 bpm (repaus) sau < 45 bpm
        if (data.getType() == DataType.HEART_RATE) {
            if (data.getSensorValue() > 100) {
                createAlert("Ritm cardiac ridicat detectat!", data.getUser());
            }
        }
    }

    private void createAlert(String message, User user) {
        // Aici vei salva alerta într-un tabel nou numit 'Alert'
        System.out.println("ALERTA AI pentru " + user.getFirstName() + ": " + message);
    }
}