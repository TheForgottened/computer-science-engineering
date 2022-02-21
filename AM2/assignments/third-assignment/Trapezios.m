% Regra dos Trap�zios
%   sol = Trapezios(f, a, b, n) 
%   sol = h / 2 * (f(a) + 2*s + f(b)) 
%
% INPUT:
%   f - fun��o para integrar usando o m�todo dos trap�zios
%   [a, b] - extremos do intervalo da vari�vel independente x
%   n - n�mero de subintervalos ou itera��es do m�todo
%
% OUTPUT: 
%   sol - integral definido aproximado
%
%   23/05/2020 - TheForgotten | https://github.com/TheForgottened

function sol = Trapezios(f, a, b, n) 
    h = (b - a) / n;
    
    x = a;
    s = 0;
    
    for i = 1: (n - 1)
        x = x + h;
        s = s + (2 * f(x));
    end 
    
    sol = (h / 2) * (f(a) + s + f(b));
end