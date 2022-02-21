%Interface02PVI para o método de Euler
%
%CHAMADA DE FUNÇÕES:
% » NEuler
% » dsolve
%
%   03/03/2020 - ArménioCorreia .: armenioc@isec.pt 
%%
clc
clear

disp('----- Métodos Numéricos para EDO/PVI ------ ');
disp(' ');
disp('----- Dados do PVI ------------------------ ');

while 1
    strF=input('f(t,y) = ', 's');
    f=@(t,y) eval(vectorize(strF));
    try
        syms t y;
        fTeste=f(t,y);
        break;
    catch me
        disp('ERRO! Introduza uma função em t e y');
    end
end

a=str2num(input('a = ','s'));
while 1
    b=str2num(input('b = ', 's'));
    if (isscalar(b) && isreal(b) && b > a)
        break;
    else
        errordlg('Introduza um número real b, com b > a',...
                 'ERRO','modal');
    end
end

n=str2num(input('n = ','s'));
y0=str2num(input('y0 = ','s'));
%%

yEuler=NEuler(f,a,b,n,y0);
yEuler_v2 = NEuler_v2(f,a,b,n,y0);

sExata=dsolve(['Dy=', strF],...
              ['y(',num2str(a),')=',num2str(y0)]);
g=@(t) eval(vectorize(char(sExata)));
h=(b-a)/n;
t=a:h:b;
yExata=g(t);

erroEuler=abs(yExata-yEuler);
erroEuler_v2=abs(yExata-yEuler_v2);
tabela=[t.', yEuler.', yEuler_v2.', yExata.', erroEuler.', erroEuler_v2.'];
disp('----- Solução do PVI em modo tabela de valores ----')
disp(tabela)

input('Prima numa tecla para continuar')
disp('-----Solução em modo gráfico----')
plot(t,yExata, '-r')
hold on
plot(t,yEuler,'b')
plot(t,yEuler_v2,'g')
hold off
grid on
legend('Exata','Euler', 'Euler_V2')
shg;