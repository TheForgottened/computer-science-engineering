% Regra de Simpson
%   [t, sol] = Simpson(f, a, b, n) 
%   sol = h / 3 * (f(a) + s + f(b))
%
% INPUT:
%   f - fun��o para integrar usando o m�todo de Simpson
%   [a, b] - extremos do intervalo da vari�vel independente x
%   n - n�mero de subintervalos ou itera��es do m�todo
%
% OUTPUT: 
%   sol - integral definido aproximado
%
%   23/05/2020 - TheForgotten | https://github.com/TheForgottened

function sol = Simpson(f, a, b, n)  
    h = (b - a) / n;
    
    x = a;
    s = 0;
    
    for i = 1: (n - 1)
        x = x + h;
        
        if (mod(i, 2) == 0)
            s = s + (2 * f(x));
        else
            s = s + (4 * f(x));
        end
    end
    
    sol = (h / 3) * (f(a) + s + f(b));
end