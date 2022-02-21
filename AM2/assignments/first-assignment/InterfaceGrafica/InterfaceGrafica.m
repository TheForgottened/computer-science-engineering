% Interface Geral para os PVI (gr�fica)
% Aplica��o dos variados m�todos num�ricos em MatLab
% Autor: TheForgotten | https://github.com/TheForgottened
% Data: 23/04/2020

function varargout = InterfaceGrafica(varargin)
% INTERFACEGRAFICA MATLAB code for InterfaceGrafica.fig
%      INTERFACEGRAFICA, by itself, creates a new INTERFACEGRAFICA or raises the existing
%      singleton*.
%
%      H = INTERFACEGRAFICA returns the handle to a new INTERFACEGRAFICA or the handle to
%      the existing singleton*.
%
%      INTERFACEGRAFICA('CALLBACK',hObject,eventData,handles,...) calls the local
%      function named CALLBACK in INTERFACEGRAFICA.M with the given input arguments.
%
%      INTERFACEGRAFICA('Property','Value',...) creates a new INTERFACEGRAFICA or raises the
%      existing singleton*.  Starting from the left, property value pairs are
%      applied to the GUI before InterfaceGrafica_OpeningFcn gets called.  An
%      unrecognized property name or invalid value makes property application
%      stop.  All inputs are passed to InterfaceGrafica_OpeningFcn via varargin.
%
%      *See GUI Options on GUIDE's Tools menu.  Choose "GUI allows only one
%      instance to run (singleton)".
%
% See also: GUIDE, GUIDATA, GUIHANDLES

% Edit the above text to modify the response to help InterfaceGrafica

% Last Modified by GUIDE v2.5 23-Apr-2020 18:27:49

% Begin initialization code - DO NOT EDIT
gui_Singleton = 1;
gui_State = struct('gui_Name',       mfilename, ...
                   'gui_Singleton',  gui_Singleton, ...
                   'gui_OpeningFcn', @InterfaceGrafica_OpeningFcn, ...
                   'gui_OutputFcn',  @InterfaceGrafica_OutputFcn, ...
                   'gui_LayoutFcn',  [] , ...
                   'gui_Callback',   []);
if nargin && ischar(varargin{1})
    gui_State.gui_Callback = str2func(varargin{1});
end

if nargout
    [varargout{1:nargout}] = gui_mainfcn(gui_State, varargin{:});
else
    gui_mainfcn(gui_State, varargin{:});
end

clc;
clear;

% End initialization code - DO NOT EDIT
end


% --- Executes just before InterfaceGrafica is made visible.
function InterfaceGrafica_OpeningFcn(hObject, eventdata, handles, varargin)
% This function has no output args, see OutputFcn.
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
% varargin   command line arguments to InterfaceGrafica (see VARARGIN)

% Choose default command line output for InterfaceGrafica
handles.output = hObject;

% Update handles structure
guidata(hObject, handles);

% UIWAIT makes InterfaceGrafica wait for user response (see UIRESUME)
% uiwait(handles.GUI);

MyAtualizar(handles);

rmpath('./InterfaceDeTexto');
end


% --- Outputs from this function are returned to the command line.
function varargout = InterfaceGrafica_OutputFcn(hObject, eventdata, handles) 
% varargout  cell array for returning output args (see VARARGOUT);
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Get default command line output from handles structure
varargout{1} = handles.output;
end


% --- Executes when user attempts to close GUI.
function GUI_CloseRequestFcn(hObject, eventdata, handles)
% hObject    handle to GUI (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: delete(hObject) closes the figure
S = questdlg('Deseja sair?','SAIR','Sim','N�o','Sim');

if strcmp(S,'N�o')
    return;
end

delete(hObject);
end



function tbFuncao_Callback(hObject, eventdata, handles)
% hObject    handle to tbFuncao (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of tbFuncao as text
%        str2double(get(hObject,'String')) returns contents of tbFuncao as a double
end


% --- Executes during object creation, after setting all properties.
function tbFuncao_CreateFcn(hObject, eventdata, handles)
% hObject    handle to tbFuncao (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
end



function tbA_Callback(hObject, eventdata, handles)
% hObject    handle to tbA (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of tbA as text
%        str2double(get(hObject,'String')) returns contents of tbA as a double
end


% --- Executes during object creation, after setting all properties.
function tbA_CreateFcn(hObject, eventdata, handles)
% hObject    handle to tbA (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
end



function tbB_Callback(hObject, eventdata, handles)
% hObject    handle to tbB (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of tbB as text
%        str2double(get(hObject,'String')) returns contents of tbB as a double
end


% --- Executes during object creation, after setting all properties.
function tbB_CreateFcn(hObject, eventdata, handles)
% hObject    handle to tbB (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
end



function tbN_Callback(hObject, eventdata, handles)
% hObject    handle to tbN (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of tbN as text
%        str2double(get(hObject,'String')) returns contents of tbN as a double
end


% --- Executes during object creation, after setting all properties.
function tbN_CreateFcn(hObject, eventdata, handles)
% hObject    handle to tbN (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
end



function tbY0_Callback(hObject, eventdata, handles)
% hObject    handle to tbY0 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of tbY0 as text
%        str2double(get(hObject,'String')) returns contents of tbY0 as a double
end


% --- Executes during object creation, after setting all properties.
function tbY0_CreateFcn(hObject, eventdata, handles)
% hObject    handle to tbY0 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
end


% --- Executes on button press in pbAtualizar.
function pbAtualizar_Callback(hObject, eventdata, handles)
% hObject    handle to pbAtualizar (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

MyAtualizar(handles);
end

% --- Executes on button press in rbRK4.
function rbRK4_Callback(hObject, eventdata, handles)
% hObject    handle to rbRK4 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of rbRK4
end

% --- Executes on button press in rbTodos.
function rbTodos_Callback(hObject, eventdata, handles)
% hObject    handle to rbTodos (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of rbTodos
end

% --- Executes on button press in rbODE45.
function rbODE45_Callback(hObject, eventdata, handles)
% hObject    handle to rbODE45 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of rbODE45
end

% --- Executes on button press in rbAdams.
function rbAdams_Callback(hObject, eventdata, handles)
% hObject    handle to rbAdams (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of rbAdams
end

% --- Executes on button press in rbRK2.
function rbRK2_Callback(hObject, eventdata, handles)
% hObject    handle to rbRK2 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of rbRK2
end

% --- Executes on button press in rbEulerM.
function rbEulerM_Callback(hObject, eventdata, handles)
% hObject    handle to rbEulerM (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of rbEulerM
end

% --- Executes on button press in rbEuler.
function rbEuler_Callback(hObject, eventdata, handles)
% hObject    handle to rbEuler (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of rbEuler
end


% --------------------------------------------------------------------
function mVoltar_Callback(hObject, eventdata, handles)
% hObject    handle to mVoltar (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
delete(handles.GUI);
MenuPrincipal();
end

function MyAtualizar(handles)
strF = get(handles.tbFuncao, 'String');
f = @(t,y) eval(vectorize(strF));

a = str2num(get(handles.tbA, 'String'));
b = str2num(get(handles.tbB, 'String'));
n = str2num(get(handles.tbN, 'String'));
y0 = str2num(get(handles.tbY0, 'String'));

escolhabg = get(handles.bgMetodos, 'SelectedObject');
escolha = find([handles.rbEuler,...
              handles.rbEulerM,...
              handles.rbRK2,...
              handles.rbRK4,...
              handles.rbAdams,...
              handles.rbODE45,...
              handles.rbTodos]==escolhabg);
          
testeFunc = MException('MATLAB:MyAtualizar:badFunc',....
                        'Introduza uma fun��o em t e y!');
                    
testeNReal = MException('MATLAB:MyAtualizar:badNReal',....
                        'Introduza um n�mero real!');
                    
testeNNatural = MException('MATLAB:MyAtualizar:badNNatural',....
                        'Introduza um n�mero natural!');
                    
testeB = MException('MATLAB:MyAtualizar:badB',....
                        'Introduza um n�mero maior que a!');
          
try  
    try
        fTeste=f(sym('t'),sym('y'));  
    catch
        set(handles.tbFuncao, 'BackgroundColor', 'red');
        throw(testeFunc);
    end
    
    set(handles.tbFuncao, 'BackgroundColor', 'white');

    if ~(isscalar(a) && isreal(a))
        set(handles.tbA, 'BackgroundColor', 'red');
        throw(testeNReal);
    else
        set(handles.tbA, 'BackgroundColor', 'white');
    end
    
    if (isscalar(b) && isreal(b))
        if ~(b > a)
            set(handles.tbB, 'BackgroundColor', 'red');
            throw(testeB);
        end
        set(handles.tbB, 'BackgroundColor', 'white');
    else
        set(handles.tbB, 'BackgroundColor', 'red');
        throw(testeNReal);
    end
    
    if ~(isscalar(n) && fix(n) == n && n > 0)
        set(handles.tbN, 'BackgroundColor', 'red');
        throw(testeNNatural);
    else
        set(handles.tbN, 'BackgroundColor', 'white');
    end
    
    if ~(isscalar(y0) && isreal(y0))
        set(handles.tbY0, 'BackgroundColor', 'red');
        throw(testeNReal);
    else
        set(handles.tbY0, 'BackgroundColor', 'white');
    end
    
    EULER = 1;
    EULERM = 2;
    RK2 = 3;
    RK4 = 4;
    ADAMS = 5;
    ODE_45 = 6;
    TODOS = 7;
    
    sExata = dsolve(['Dy =', strF], ['y(', num2str(a), ') = ', num2str(y0)]);
    g = @(t) eval(vectorize(char(sExata)));
    h = (b - a) / n;
    t = a: h: b;
    yExata = g(t);
    
    yEuler = NEuler(f, a, b, n, y0);
    yEulerErro = abs(yExata - yEuler);
    
    yEulerM = NEuler_M(f, a, b, n, y0);
    yEulerMErro = abs(yExata - yEulerM);
    
    yRK2 = NRK2(f, a, b, n, y0);
    yRK2Erro = abs(yExata - yRK2);
    
    yRK4 = NRK4(f, a, b, n, y0);
    yRK4Erro = abs(yExata - yRK4);
    
    yAdams = NAdams(f, a, b, n, y0);
    yAdamsErro = abs(yExata - yAdams);
    
    [~, yODE45] = ode45(f, t, y0);
    yODE45 = yODE45.';
    yODE45Erro = abs(yExata - yODE45);
    
    plot(t,yExata);
    hold on;
    
    switch escolha
        case EULER
            plot(t, yEuler, 'r');
            legend('yExata','yEuler');
            tabela = [t.', yExata.', yEuler.', yEulerErro.']; 
            set(handles.uiTabela, 'Data', num2cell(tabela)); 
            set(handles.uiTabela, 'ColumnName',...
                    [{'t'}, {'Exata'}, {'Euler'}, {'Erro'}]);
                
        case EULERM
            plot(t, yEulerM, 'r');
            legend('yExata', 'yEulerM');
            tabela = [t.', yExata.', yEulerM.', yEulerMErro.']; 
            set(handles.uiTabela, 'Data', num2cell(tabela)); 
            set(handles.uiTabela, 'ColumnName',...
                    [{'t'}, {'Exata'}, {'EulerM'}, {'Erro'}]);
                
        case RK2
            plot(t, yRK2, 'r');
            legend('yExata', 'yRK2');
            tabela = [t.', yExata.', yRK2.', yRK2Erro.']; 
            set(handles.uiTabela, 'Data', num2cell(tabela)); 
            set(handles.uiTabela, 'ColumnName',...
                    [{'t'}, {'Exata'}, {'RK2'}, {'Erro'}]);
                
        case RK4
            plot(t, yRK4, 'r');
            legend('yExata', 'yRK4');
            tabela = [t.', yExata.', yRK4.', yRK4Erro.']; 
            set(handles.uiTabela, 'Data', num2cell(tabela)); 
            set(handles.uiTabela, 'ColumnName',...
                    [{'t'}, {'Exata'}, {'RK4'}, {'Erro'}]);
                
        case ADAMS
            plot(t, yAdams, 'r');
            legend('yExata', 'yAdams');
            tabela = [t.', yExata.', yAdams.', yAdamsErro.']; 
            set(handles.uiTabela, 'Data', num2cell(tabela)); 
            set(handles.uiTabela, 'ColumnName',...
                    [{'t'}, {'Exata'}, {'Adams'}, {'Erro'}]);
                
        case ODE_45
            plot(t, yODE45, 'r');
            legend('yExata', 'yODE45');
            tabela = [t.', yExata.', yODE45.', yODE45Erro.']; 
            set(handles.uiTabela, 'Data', num2cell(tabela)); 
            set(handles.uiTabela, 'ColumnName',...
                    [{'t'}, {'Exata'}, {'ODE45'}, {'Erro'}]);
                
        case TODOS
            plot(t, yEuler, 'Color', [0 1 0]);
            plot(t, yEulerM, 'Color', [1 0 0]);
            plot(t, yRK2, 'Color', [1 1 0]);
            plot(t, yRK4, 'Color', [1 0 1]);
            plot(t, yAdams, 'Color', [0 1 1]);
            plot(t, yODE45, 'Color', [0.6980 0.1333 0.1333]);
            
            tabela = [t.', yExata.', yEuler.', yEulerM.', ...
                        yRK2.', yRK4.', yAdams.', yODE45.', ...
                        yEulerErro.', yEulerMErro.', yRK2Erro.', ...
                        yRK4Erro.', yAdamsErro.', yODE45Erro.'];
                    
            set(handles.uiTabela, 'Data', num2cell(tabela)); 
            
            set(handles.uiTabela, 'ColumnName',...
                    [{'t'}, {'Exata'}, {'Euler'}, {'EulerM'}, ... 
                     {'RK2'},  {'RK4'}, {'Adams'}, ...
                     {'ODE45'}, {'Erro Euler'}, {'Erro EulerM'}, ...
                     {'Erro RK2'}, {'Erro RK4'}, {'Erro Adams'}, ...
                     {'Erro ODE45'}]);
                
            legend('yExata', 'yEuler', 'yEulerM', 'yRK2', 'yRK4', ...
                    'yAdams', 'yODE45');
    end
    
    hold off;
    grid on;

catch Me
    errordlg(Me.message,'ERRO','modal');
end

end
