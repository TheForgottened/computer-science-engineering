function paridade1( )
%Funcao percepcao3a: cria, treina e testa um perceptrao
%usando as funcoes da NNTool

% limpar
clear all;
close all;

% inicializar entrada
p = [0 0 0 0 0 0 0 0 1 1 1 1 1 1 1 1;
     0 0 0 0 1 1 1 1 0 0 0 0 1 1 1 1;
     0 0 1 1 0 0 1 1 0 0 1 1 0 0 1 1;
     0 1 0 1 0 1 0 1 0 1 0 1 0 1 0 1];

t = [1 0 0 1 0 1 1 0 0 1 1 0 1 0 0 1];

% COMPLETAR: criar um perceptrao chamado net
net = feedforwardnet;

% FUNCAO DE ATIVACAO DA CAMADA DE SAIDA
net.layers{2}.transferFcn = 'tansig';

% FUNCAO DE TREINO 
net.trainFcn = 'trainlm';

% COMPLETAR: Numero de epocas de treino: 100
net.trainParam.epochs = 100;

% TODOS OS EXEMPLOS DE INPUT SAO USADOS NO TREINO
net.divideFcn = '';      

% COMPLETAR: treinar a rede
net = train(net, p, t);

% COMPLETAR: simular a rede e guardar o resultado na variavel y
y = sim(net, p);

% Mostrar resultado
fprintf('Saida do perceptrao para %s:', op);
disp(y);
fprintf('Saida desejada para %s:', op);
disp(t);

% visualizar a arquitetura da rede criada
view(net)

%Plot 
w = net.iw{1,1};
b = net.b{1};
plotpv(p, t)
plotpc(w, b)

end

