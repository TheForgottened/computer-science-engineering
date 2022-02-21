%Interface01 para o método de Euler
%
%CHAMADA DE FUNÇÕES:
% » NEuler
% » dsolve
%
%   03/03/2020 - ArménioCorreia .: armenioc@isec.pt 

% clc
clear

disp('----- Método de Euler para EDO ------ ');
disp(' ');
 
% sExata = dsolve('Dy=y+exp(3*t)')
% sExataP = dsolve('Dy=y+exp(3*t)', 'y(1)=2')

strF='y + exp(3*t)';
f = @(t,y)eval(vectorize(strF));

a  = 0;
b  = 1.5;
n  = 3;
y0 = 2;

yEuler = NEuler(f, a, b, n, y0);

sExata=dsolve(['Dy=', strF],...
                ['y(',num2str(a),')=',num2str(y0)]);
g=@(t) eval(vectorize(char(sExata)));
h=(b-a)/n;
t=a:h:b;
yExata=g(t);

erroEuler=abs(yExata-yEuler);
tabela=[t.', yEuler.', yExata.', erroEuler.'];
disp('----- Solução do PVI em modo tabela de valores ----')
disp(tabela)

input('Prima numa tecla para continuar')
disp('----- Solução do PVI em modo gráfico ----')
plot(t,yEuler,'-r')
hold on
plot(t,yExata, 'b')
hold off
grid on
legend('Euler','Exata')
shg;