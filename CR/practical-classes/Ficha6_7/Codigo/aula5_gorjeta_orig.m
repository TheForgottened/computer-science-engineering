function [ fis_gorjeta ] = aula_gorjeta()

%PASSO 1: crie a estrutura FIS de nome fis_gorjeta
%COMPLETAR


%PASSO 2: criar variaveis linguisticas ?servico?, ?comida? e ?gorjeta?
fis_gorjeta=addvar(fis_gorjeta,'input','servico',[0 10]);
%COMPLETAR

%PASSO 3: fun??es de perten?a para cada vari?vel criada anteriormente
fis_gorjeta=addmf(fis_gorjeta,'input',1,'fraco','gaussmf',[1.5 0]);
%COMPLETAR

fis_gorjeta=addmf(fis_gorjeta,'input',2,'ma','trapmf',[0 0 1 3]);
%COMPLETAR
 
fis_gorjeta=addmf(fis_gorjeta,'output',1,'fraca','trimf',[0 5 10]);
%COMPLETAR
 
%PASSO 4: criar matriz de regras e adicionar com addrule

regras=[];%COMPLETAR


%PASSO 5: avaliar para v?rios valores de service e comida com evalfis
for servico=0:10
    for comida=0:10
        entrada=[servico comida];
        out = evalfis(entrada,fis_gorjeta);
        fprintf('servi?o = %d\nComida = %d\nGorjeta = %f\n\n',servico, comida, out);
    end
end
 
end
