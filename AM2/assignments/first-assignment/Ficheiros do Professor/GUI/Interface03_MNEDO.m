%INTERFACE03_MNEDO
%   y'= f(t,y) com t=[a, b] e y(a)=y0 condição inicial  
%
%CHAMADA FUNÇÕES: N_Euler; N_RK2; dsolve
%
%   02/11/2015 - ArménioCorreia .: armenioc@isec.pt 

clc
clear

disp('--------------------------');
disp('--------MN para PVI-------');
disp('--------------------------');

disp('--Parâmetros de entrada---');

strF=input('f(t,y)= ','s');
f   =@(t,y) eval(vectorize(strF));
a   = str2num(input('a= ','s'));
b   = str2num(input('b= ','s'));
n   = str2num(input('n= ','s'));
y0  = str2num(input('y0= ','s'));


disp('---- Método de Euler ---');
yEuler=N_Euler(f,a,b,n,y0);
disp(yEuler);

disp('---- Método de RK2 ----');
yRK2=N_RK2(f,a,b,n,y0);
disp(yRK2);

disp('---- Solução exata----');
sExata=dsolve(['Dy=',strF],...
      ['y(',num2str(a),')=',num2str(y0)]);
t=a:(b-a)/n:b;
yExata=eval(vectorize(char(sExata)));
disp(yExata);

disp('---- Tabela de Resultados----');
erroEuler=abs(yExata-yEuler);
erroRK2=abs(yExata-yRK2);

tabela=[t.',yExata.',yEuler.',yRK2.',...
        erroEuler.',erroRK2.'];
disp(tabela);

plot(t,yExata);
hold on
plot(t,yEuler,'r');
plot(t,yRK2,'g');
hold off
legend('yExata','yEuler','yRK2');
shg