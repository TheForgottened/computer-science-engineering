% Fun��o que pede um escalar real ao utilizador.
% Esta verifica se � v�lido e, se n�o for, fica em loop at� ser introduzido
% um valor v�lido.
%
% INPUT:
%   s - nome da vari�vel (string)
%
% OUTPUT:
%   n - valor obtido
%
% Autor: TheForgotten | https://github.com/TheForgottened
% Data: 07/04/2020

function n = F_PedirNumero(s)

    while 1
        n = input([s, ' = '], 's');
        
        [temp, b] = str2num(n);

        if(b && isscalar(temp) && isreal(temp))
            n = temp;
            break;
        else
            fprintf(2, 'N�mero inv�lido!\n\n');
        end
    end
end
