# bleYan
It's a simple BLE library.    
Android apk download:http://fir.im/bleYan    
For iOS (BabyBluetooth) https://github.com/coolnameismy/BabyBluetooth   
##  FF. bleYan tools
![ble_yan_screenshot](/screenshot/screenshot.jpg?raw=true "ble_scan_screenshot")

##  0. Usage
In order to use the library, Gradle dependency:

 - 	Add the following to your `build.gradle`:
 ```gradle
repositories {
	    maven { url "https://oss.sonatype.org/content/repositories/snapshots" }
}

dependencies {
	    compile 'com.github.captain-miao:ble:1.1.0-SNAPSHOT'
}
```

##  1. create Bluetooth Low Energy service
```
public class AppBleService extends BaseBleService {
    private final static String TAG = AppBleService.class.getName();

    //after discoverServices, can do something()
    @Override
    public void onDiscoverServices(BluetoothGatt gatt) {
        doSomething()
    }

    /**
     * BleService scan bluetooth device Callback
     *
     * @param device     Identifies the remote device
     * @param rssi       The RSSI value for the remote device as reported by the
     *                   Bluetooth hardware. 0 if no RSSI value is available.
     * @param scanRecord The content of the advertisement record offered by
     */
    @Override
    public void onBleScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        
    }

    /**
     *
     * @param scanState Error code (one of SCAN_FAILED_*) for scan failure.
     */
    @Override
    public void onBleScanFailed(BleScanState scanState) {

    }
}
```
##2. create BluetoothHelper
```
public class AppBluetoothHelper extends BluetoothHelper {
    private final static String TAG = AppBluetoothHelper.class.getName();

    public AppBluetoothHelper(Context context) {
        super(context);
    }

    @Override
    public boolean bindService(OnBindListener bindListener) {
        this.mBindListener = bindListener;
        return context.getApplicationContext().bindService(
                new Intent(context, AppBleService.class), this, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void unbindService() {
        context.getApplicationContext().unbindService(this);
    }

})
```
##3.Scan Bluetooth Device
```
mBleScanner = new BleScanner(activity, new SimpleScanCallback() {
	    @Override
	    public void onBleScan(BluetoothDevice bluetoothDevice, int rssi, byte[] scanRecord) {

	    }
	 
	    @Override
	    public void onBleScanFailed(BleScanState scanState) {
	       //scanState error code
	    }
	});
	//scan always
	mBleScanner.startBleScan();
	//when timeout, call onBleScanFailed()
	mBleScanner.onStartBleScan(long timeoutMillis);
	//stop scan
	mBleScanneronStopBleScan();
```

##4.Connect Bluetooth Device
```
mBleHelper = new AppBluetoothHelper(Context context, BleCallback bleCallback);
	mBleHelper.bindService(new AppBluetoothHelper.OnBindListener() {
	    @Override
	    public void onServiceConnected() {
	        connectDevice(String deviceMac);
	    }
	});
	public void connectDevice(final String deviceMac) {
	  mBleHelper.connectDevice(deviceMac, new ConnectCallback() {
	      @Override
	      public void onConnectSuccess() {
	          mBleHelper.mConnCallback = null;//cancel callback
	           
	      }
	 
	      @Override
	      public void onConnectFailed(ConnectError error) {
	 
	      }
	  });
	}
```
##4.write/read Bluetooth Device
```
	//writeCharacteristic
	mBleHelper.writeCharacteristic(UUID serviceUUID, UUID CharacteristicUUID, byte[] values)
	//CharacteristicNotification
	mBleHelper.updateCharacteristicNotification(UUID serviceUUID, UUID characteristicUUID, UUID descriptorUUID, boolean enable)
	//readCharacteristic
	mBleHelper.readFromCharacteristic(UUID serviceUUID, UUID CharacteristicUUID) 
	//bleCallback
	BluetoothHelper(Context context, BleCallback bleCallback)
	//Callback triggered as a result of a remote characteristic notification.
	//BluetoothGattCallback#onCharacteristicChanged --> onCharacteristicNotification
	public void onCharacteristicNotification(UUID uuid, byte[] data) {
	 
	}
	 
	//Callback reporting the result of a characteristic read operation.
	//BluetoothGattCallback#onCharacteristicChanged
	public void onCharacteristicRead(UUID uuid, byte[] data) {
	 
	}
	 
	//Callback indicating the result of a characteristic write operation.
	//BluetoothGattCallback#onCharacteristicWrite
	public void onCharacteristicWrite(UUID uuid, int status) {
	 
	}
```
##4.release Bluetooth Device
```
	mBleHelper.release();
```
usage(Chinese):https://yanlu.me/android-bluetooth-low-energy/  
apk download:http://fir.im/bleYan  
QQ Group:436275452
