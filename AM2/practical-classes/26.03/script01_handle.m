clc
clear

x = 0: 0.1: 2 * pi;
y = sin(x);

h1 = plot(x, y);

ishandle(h1)
get(h1)

tipoH1=get(h1, 'Type')

inspect(h1)

set(h1, 'Color', [1, 0, 0], 'LineWidth', 3)