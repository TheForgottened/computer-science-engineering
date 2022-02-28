import 'dart:math';

import 'package:example/cat_screen.dart';
import 'package:example/location_screen.dart';
import 'package:flutter/material.dart';
import 'package:location/location.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        title: 'Flutter Demo',
        theme: ThemeData(
          // This is the theme of your application.
          //
          // Try running your application with "flutter run". You'll see the
          // application has a blue toolbar. Then, without quitting the app, try
          // changing the primarySwatch below to Colors.green and then invoke
          // "hot reload" (press "r" in the console where you ran "flutter run",
          // or simply save your changes to "hot reload" in a Flutter IDE).
          // Notice that the counter didn't reset back to zero; the application
          // is not restarted.
          primarySwatch: Colors.blue,
        ),
        // home: const MyHomePage(title: 'Flutter Demo Home Page'),
        initialRoute: MyHomePage.routeName,
        routes: {
          MyHomePage.routeName: (_) =>
              const MyHomePage(title: 'Flutter Demo Home Page'),
          CatScreen.routeName: (_) => const CatScreen(),
          LocationScreen.routeName: (_) => const LocationScreen(),
        });
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({Key? key, required this.title}) : super(key: key);
  static const String routeName = 'MyHomePage';

  // This widget is the home page of your application. It is stateful, meaning
  // that it has a State object (defined below) that contains fields that affect
  // how it looks.

  // This class is the configuration for the state. It holds the values (in this
  // case the title) provided by the parent (in this case the App widget) and
  // used by the build method of the State. Fields in a Widget subclass are
  // always marked "final".

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  int _counter = 0;
  Color? _backgroundColor;

  @override
  void initState() {
    super.initState();
    debugPrint('Estado iniciado: Só é chamado uma vez por inicialização.');
  }

  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    debugPrint(
        'Dependências é alteradas: Só é chamado quando uma dependência muda, ou na primeira vez que é iniciado o estado, após o initState.');
  }

  @override
  void didUpdateWidget(covariant MyHomePage oldWidget) {
    super.didUpdateWidget(oldWidget);
    debugPrint(
        'Widget atualizado, estado atualizado: Sempre que é atualizado o widget dono deste estado é invococado este método. Podem ser comparadas as propriedades do oldWidget com o widget atual.');
  }

  @override
  void dispose() {
    super.dispose();
    debugPrint(
        'Widget destruído: Só é chamado uma vez, quando o widget é destruído e, correspondentemente, o seu estado.');
  }

  void _incrementCounter() async {
    setState(() {
      // This call to setState tells the Flutter framework that something has
      // changed in this State, which causes it to rerun the build method below
      // so that the display can reflect the updated values. If we changed
      // _counter without calling setState(), then the build method would not be
      // called again, and so nothing would appear to happen.
      _counter++;
      _updateBackgroundColor();
    });

    if (_counter == 20) {
      // Navigator.of(context)
      //    .push(MaterialPageRoute(builder: (_) => const CatScreen()));
      _counter = await Navigator.push(
        context,
        MaterialPageRoute(
          builder: (_) => const CatScreen(),
          settings: RouteSettings(arguments: _counter),
        ),
      );
      setState(() {});
    }
  }

// Primeiro desafio: atualizar a cor
  void _updateBackgroundColor() {
    _backgroundColor = Color.fromRGBO(Random().nextInt(255),
        Random().nextInt(255), Random().nextInt(255), 1.0);
  }

  // Desafio 2: Decrementar o contador
  void _decrementCounter() {
    if (_counter == 0) {
      return;
    }

    setState(() {
      _counter--;
      _updateBackgroundColor();
    });
  }

  @override
  Widget build(BuildContext context) {
    // This method is rerun every time setState is called, for instance as done
    // by the _incrementCounter method above.
    //
    // The Flutter framework has been optimized to make rerunning build methods
    // fast, so that you can just rebuild anything that needs updating rather
    // than having to individually change instances of widgets.
    return Scaffold(
      backgroundColor: _backgroundColor,
      appBar: AppBar(
        // Here we take the value from the MyHomePage object that was created by
        // the App.build method, and use it to set our appbar title.
        title: Text(widget.title),
      ),
      body: Center(
        // Center is a layout widget. It takes a single child and positions it
        // in the middle of the parent.
        child: Column(
          // Column is also a layout widget. It takes a list of children and
          // arranges them vertically. By default, it sizes itself to fit its
          // children horizontally, and tries to be as tall as its parent.
          //
          // Invoke "debug painting" (press "p" in the console, choose the
          // "Toggle Debug Paint" action from the Flutter Inspector in Android
          // Studio, or the "Toggle Debug Paint" command in Visual Studio Code)
          // to see the wireframe for each widget.
          //
          // Column has various properties to control how it sizes itself and
          // how it positions its children. Here we use mainAxisAlignment to
          // center the children vertically; the main axis here is the vertical
          // axis because Columns are vertical (the cross axis would be
          // horizontal).
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            // Desafio 3: Flutter Logo, esta abordagem permite que vejam uma animação implícita do widget do Flutter Logo (ao animar o scale de 0 para 200)
            FlutterLogo(
              size: _counter >= 10 ? 200.0 : 0.0,
            ),
            // Outra maneira de inserir o widget com base na condição. Ao contrário do exemplo anterior, o widget aqui só é
            // inserido mesmo quando a condição é válida, enquanto que no outro caso apenas muda o tamanho de 0 até 200 com base na condição.
            /* 
            if(_counter >= 10) const FlutterLogo(
              size:200.0,
            ),*/
            const Text(
              'You have pushed the button this many times:',
            ),
            Text(
              '$_counter',
              style: Theme.of(context).textTheme.headline4,
            ),
            // Segundo desafio: adicionar botões
            ElevatedButton(
              onPressed: () => setState(() => _counter = 0),
              child: const Text('Reset'),
            ),
            ElevatedButton(
              onPressed: () => Navigator.pushNamed(context, CatScreen.routeName,
                  arguments: _counter),
              child: const Text('Explicit'),
            ),
            ElevatedButton(
              onPressed: () => Navigator.pushNamed(
                  context, LocationScreen.routeName,
                  arguments: _counter),
              child: const Text('Location Screen'),
            ),
          ],
        ),
      ),
      floatingActionButton: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: [
          // Desafio 2: Decrementar o contador
          Padding(
            padding: const EdgeInsets.only(left: 25.0),
            child: FloatingActionButton(
              heroTag: 'decrement',
              onPressed: _decrementCounter,
              tooltip: 'Decrement',
              child: const Icon(Icons.remove),
            ),
          ),
          FloatingActionButton(
            heroTag: 'increment',
            onPressed: _incrementCounter,
            tooltip: 'Increment',
            child: const Icon(Icons.add),
          ),
        ],
      ), // This trailing comma makes auto-formatting nicer for build methods.
    );
  }
}

class Teste extends StatelessWidget {
  const Teste({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Container();
  }
}
