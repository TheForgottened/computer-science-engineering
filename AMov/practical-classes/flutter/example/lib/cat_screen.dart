import 'dart:convert';
import 'dart:io';

import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;

class CatScreen extends StatefulWidget {
  const CatScreen({Key? key}) : super(key: key);
  static const String routeName = 'CatScreen';

  @override
  State<StatefulWidget> createState() => _CatScreenState();
}

class CatFact {
  CatFact.fromJson(Map<String, dynamic> json)
      : fact = json['fact'],
        length = json['length'];

  final String fact;
  final int length;
}

class _CatScreenState extends State<CatScreen> {
  late final int _counter =
      ModalRoute.of(context)?.settings.arguments as int ?? 0;

  bool _fetchingData = false;
  static const String _catFactsUrl = 'https://catfact.ninja/facts';
  List<CatFact>? _catFacts;

  Future<void> _fetchCatFacts() async {
    try {
      setState(() => _fetchingData = true);
      http.Response response = await http.get(Uri.parse(_catFactsUrl));

      if (response.statusCode == HttpStatus.ok) {
        debugPrint(response.body);
        final Map<String, dynamic> decodedData = json.decode(response.body);
        setState(() => _catFacts = (decodedData['data'] as List)
            .map((fact) => CatFact.fromJson(fact))
            .toList());
      }
    } catch (ex) {
      debugPrint('Something went wrong: $ex');
    } finally {
      setState(() => _fetchingData = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Cat Screen'),
      ),
      body: Center(
        child: Column(
          children: [
            const Text('This is Cat Screen'),
            Text('_counter: $_counter'),
            ElevatedButton(
              onPressed: () => Navigator.pop(context, _counter * 2),
              child: const Text('Return'),
            ),
            FutureBuilder<String>(
                future: _fetchString(),
                builder: (context, snapshot) {
                  if (snapshot.hasError) {
                    return const Text('Ups, failed!');
                  } else if (snapshot.hasData) {
                    return Text('Texto: $snapshot.data');
                  } else {
                    return const CircularProgressIndicator();
                  }
                }),
            ElevatedButton(
                onPressed: () => _fetchCatFacts(),
                child: const Text('Fetch Cat Facts')),
            if (_fetchingData)
              const CircularProgressIndicator()
            else if (_catFacts != null && _catFacts!.isNotEmpty)
              Expanded(
                child: ListView.separated(
                    itemBuilder: (context, index) => ListTile(
                          title: Text('Cat fact #${index + 1}'),
                          subtitle: Text(_catFacts![index].fact),
                          onTap: () => debugPrint("Carregou em $index"),
                        ),
                    separatorBuilder: (_, __) => const Divider(
                          thickness: 5,
                          indent: 10,
                        ),
                    itemCount: _catFacts!.length),
              )
          ],
        ),
      ),
    );
  }

  Future<String> _fetchString() async {
    await Future.delayed(const Duration(seconds: 5));
    return Future.value('My complicated info.');
  }
}
