import 'package:flutter/material.dart';
import 'package:location/location.dart';

class LocationScreen extends StatefulWidget {
  const LocationScreen({Key? key}) : super(key: key);
  static const String routeName = 'LocationScreen';

  @override
  State<StatefulWidget> createState() => _LocationScreenState();
}

class _LocationScreenState extends State<LocationScreen> {
  Location location = Location();

  bool _serviceEnabled = false;
  PermissionStatus _permissionGranted = PermissionStatus.denied;
  LocationData? _locationData;

  @override
  void initState() {
    super.initState();
    _fetchLocation();
  }

  Future<void> _fetchLocation() async {
    // Verificar estado do serviço
    _serviceEnabled = await location.serviceEnabled();
    if (!_serviceEnabled) {
      _serviceEnabled = await location.requestService();
    }

    // Pede permissões em runtime
    _permissionGranted = await location.hasPermission();
    if (_permissionGranted == PermissionStatus.denied) {
      _permissionGranted = await location.requestPermission();
    }

    await _getCoordinates();

    setState(() {});
    // Desafio de usar o onLocationChanged
    location.onLocationChanged.listen(((locationData) {
      setState(() => _locationData = locationData);
    }));
  }

  Future<void> _getCoordinates() async {
    _locationData = await location.getLocation();
    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Location Screen')),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            if (!_serviceEnabled)
              const Text('Service not enabled')
            else if (_permissionGranted == PermissionStatus.denied)
              const Text('Permissions not granted')
            else if (_locationData == null)
              const CircularProgressIndicator()
            else
              Text('Latitude: ${_locationData!.latitude}'
                  'Longitude: ${_locationData!.longitude}'),
            StreamBuilder<LocationData>(
              stream: location.onLocationChanged,
              builder: (context, snapshot) {
                return Text('Latitude: ${_locationData!.latitude}'
                    'Longitude: ${_locationData!.longitude}');
              },
            )
          ],
        ),
      ),
    );
  }
}
