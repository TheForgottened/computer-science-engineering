#include "TV.h"

void TV::ligar() {
    ligada = true;
}

void TV::desligar() {
    ligada = false;
}

void TV::aumentarVol(int n) {
    if (vol < VMAX)
        vol += n;
}

void TV::diminuirVol(int n) {
    if (vol > VMIN)
        vol -= n;
}

void TV::mudarCanal(int n) {
    if (ligada)
        canal = n;
}

std::string TV::obterCanais() {
    std::stringstream iss;

    for (int i = 0; i < nCanais; i++)
        iss << (i + 1) << "\t" << canais[i] << std::endl;

    return iss.str();
}

std::string TV::getAsString() {
    std::stringstream iss;

    iss << "Ligada? " << ligada << "\nVol: " << vol << "\nCanal: " << canal << "\nNr de canais: " << nCanais << std::endl;

    return iss.str();
}