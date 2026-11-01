abstract class SmartDevice{
    String deviceName;
    public SmartDevice(String deviceName){
        this.deviceName = deviceName;
    }
    public abstract void turnOn();
    public abstract void turnOff();
}

class SmartLight extends SmartDevice{
    public SmartLight(String deviceName){
        super(deviceName);
    }
    @Override
    public void turnOn() {
        System.out.println(deviceName + " 电灯开启");
    }
    @Override
    public void turnOff() {
        System.out.println(deviceName + " 电灯关闭");
    }
    public void setBrightness(int level){
        System.out.println(deviceName + " 设置亮度：" + level + "%");
    
    }
}
    
class SmartAC extends SmartDevice{
    public SmartAC(String deviceName){
        super(deviceName);
    }
    @Override
    public void turnOn() {
        System.out.println(deviceName + " 空调开启");
    }
    @Override
    public void turnOff() {
        System.out.println(deviceName + " 空调关闭");
    }
    public void setTemperature(int temp){
        System.out.println(deviceName + " 设置温度：" + temp + "℃");
    }
}

class SmartHomeHub{
    public void controlDevice(SmartDevice device){
        device.turnOn();

        if(device instanceof SmartLight){
            SmartLight light = (SmartLight) device;
            light.setBrightness(80); 
        }else if(device instanceof SmartAC){
            SmartAC ac = (SmartAC) device;
            ac.setTemperature(26);
        }
    }
}

public class SmartHomeTest {
    public static void main(String[] args) {
        SmartHomeHub hub = new SmartHomeHub();
        SmartLight light = new SmartLight("客厅灯");
        SmartAC ac = new SmartAC("卧室空调");
        hub.controlDevice(light);
        hub.controlDevice(ac);
    }
}
