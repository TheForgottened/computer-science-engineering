function varargout = NIntegracao(varargin)
% NINTEGRACAO MATLAB code for NIntegracao.fig
%      NINTEGRACAO, by itself, creates a new NINTEGRACAO or raises the existing
%      singleton*.
%
%      H = NINTEGRACAO returns the handle to a new NINTEGRACAO or the handle to
%      the existing singleton*.
%
%      NINTEGRACAO('CALLBACK',hObject,eventData,handles,...) calls the local
%      function named CALLBACK in NINTEGRACAO.M with the given input arguments.
%
%      NINTEGRACAO('Property','Value',...) creates a new NINTEGRACAO or raises the
%      existing singleton*.  Starting from the left, property value pairs are
%      applied to the GUI before NIntegracao_OpeningFcn gets called.  An
%      unrecognized property name or invalid value makes property application
%      stop.  All inputs are passed to NIntegracao_OpeningFcn via varargin.
%
%      *See GUI Options on GUIDE's Tools menu.  Choose "GUI allows only one
%      instance to run (singleton)".
%
% See also: GUIDE, GUIDATA, GUIHANDLES

% Edit the above text to modify the response to help NIntegracao

% Last Modified by GUIDE v2.5 23-May-2020 05:38:17

% Begin initialization code - DO NOT EDIT
gui_Singleton = 1;
gui_State = struct('gui_Name',       mfilename, ...
                   'gui_Singleton',  gui_Singleton, ...
                   'gui_OpeningFcn', @NIntegracao_OpeningFcn, ...
                   'gui_OutputFcn',  @NIntegracao_OutputFcn, ...
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
% End initialization code - DO NOT EDIT
end

% --- Executes just before NIntegracao is made visible.
function NIntegracao_OpeningFcn(hObject, eventdata, handles, varargin)
% This function has no output args, see OutputFcn.
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
% varargin   command line arguments to NIntegracao (see VARARGIN)

% Choose default command line output for NIntegracao
handles.output = hObject;

% Update handles structure
guidata(hObject, handles);

% UIWAIT makes NIntegracao wait for user response (see UIRESUME)
% uiwait(handles.figure1);

myAtualizar(handles);
end

% --- Outputs from this function are returned to the command line.
function varargout = NIntegracao_OutputFcn(hObject, eventdata, handles) 
% varargout  cell array for returning output args (see VARARGOUT);
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Get default command line output from handles structure
varargout{1} = handles.output;
end

% --- Executes on button press in pbAtualizar.
function pbAtualizar_Callback(hObject, eventdata, handles)
% hObject    handle to pbAtualizar (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

myAtualizar(handles);
end

% --- Executes on button press in cbGrid.
function cbGrid_Callback(hObject, eventdata, handles)
% hObject    handle to cbGrid (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of cbGrid

if get(handles.cbGrid, 'Value')
    grid on;
else
    grid off;
end
end

% --- Executes on button press in cbFill.
function cbFill_Callback(hObject, eventdata, handles)
% hObject    handle to cbFill (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of cbFill
strF = get(handles.tbF, 'String');
f = @(x) eval(vectorize(strF));

a = str2num(get(handles.tbA, 'String'));
b = str2num(get(handles.tbB, 'String'));
n = str2num(get(handles.tbN, 'String'));

h = (b - a) / n;
t = a: h: b;

A = area(t, f(t));
A.FaceColor = [0.80 0 0.2];

if get(handles.cbFill, 'Value')
    alpha(A, 1);
else
    alpha(A, 0);
end

cbGrid_Callback(hObject, eventdata, handles);
cbLegend_Callback(hObject, eventdata, handles);
cbLegendAxes_Callback(hObject, eventdata, handles);
end

% --- Executes on button press in cbLegend.
function cbLegend_Callback(hObject, eventdata, handles)
% hObject    handle to cbLegend (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of cbLegend
if get(handles.cbLegend, 'Value')
    legend('f(x)');
else
    legend off;
end
end

% --- Executes on button press in cbLegendAxes.
function cbLegendAxes_Callback(hObject, eventdata, handles)
% hObject    handle to cbLegendAxes (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of cbLegendAxes

if get(handles.cbLegendAxes, 'Value')
    xlabel('Eixo dos xx');
    ylabel('Eixo dos yy');
else
    xlabel('');
    ylabel('');
end
end
    


function tbF_Callback(hObject, eventdata, handles)
% hObject    handle to tbF (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of tbF as text
%        str2double(get(hObject,'String')) returns contents of tbF as a double
end

% --- Executes during object creation, after setting all properties.
function tbF_CreateFcn(hObject, eventdata, handles)
% hObject    handle to tbF (see GCBO)
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

function myAtualizar(handles)
strF = get(handles.tbF, 'String');
f = @(x) eval(vectorize(strF));

cla reset;

a = str2num(get(handles.tbA, 'String'));
b = str2num(get(handles.tbB, 'String'));
n = str2num(get(handles.tbN, 'String'));

escolhabg = get(handles.bgMetodos, 'SelectedObject');
escolhaM = find([handles.rbTrapezios, ...
              handles.rbSimpson, ...
              handles.rbAll] == escolhabg);
          
testeFun = MException('MATLAB:MyAtualizar:badFunc',....
                        'Introduza uma função em x!');
                    
testeNReal = MException('MATLAB:MyAtualizar:badNReal',....
                        'Introduza um número real!');
                    
testeNNatural = MException('MATLAB:MyAtualizar:badNNatural',....
                        'Introduza um número natural!');
                    
testeB = MException('MATLAB:MyAtualizar:badB',....
                        'Introduza um número maior que a!');
                    
testeNPar = MException('MATLAB:MyAtualizar:badNPar', ...
                        'Introduza um número par!');
          
try
    set(handles.tbF, 'BackgroundColor', 'white');
    
    try
        fTest = f(sym('x'));
    catch
        set(handles.tbF, 'BackgroundColor', 'red');
        throw(testeFun);
    end
   
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
    
    syms x;
    sExata = int(f, x, a, b);
    sExata = double(sExata);
    
    switch escolhaM
        case 1 % Trapézios
           	sTrap = Trapezios(f, a, b, n);

            sErroTrap = abs(sTrap - sExata);
            
            tabela = [sExata.', sTrap.', sErroTrap.'];
            set(handles.uiTabelaIntNum, 'Data', num2cell(tabela));
            set(handles.uiTabelaIntNum, 'ColumnName',...
                    [{'Exata'}, {'Trapézios'}, {'Erro'}]);
        
        case 2 % Simpson
            if ~(mod(n, 2) == 0)
                set(handles.tbN, 'BackgroundColor', 'red');
                throw(testeNPar);
            end
                
            sSimp = Simpson(f, a, b, n);

            sErroSimp = abs(sSimp - sExata);
            
            tabela = [sExata.', sSimp.', sErroSimp.'];
            set(handles.uiTabelaIntNum, 'Data', num2cell(tabela));
            set(handles.uiTabelaIntNum, 'ColumnName',...
                    [{'Exata'}, {'Simpson'}, {'Erro'}]);
                
        case 3 % Todos
            if ~(mod(n, 2) == 0)
                set(handles.tbN, 'BackgroundColor', 'red');
                throw(testeNPar);
            end
            
            sTrap = Trapezios(f, a, b, n);
            sSimp = Simpson(f, a, b, n);
            
            sErroTrap = abs(sTrap - sExata);
            sErroSimp = abs(sSimp - sExata);
            
            tabela = [sExata.', sTrap.', sErroTrap.', ...
                        sSimp.', sErroSimp.'];
            set(handles.uiTabelaIntNum, 'Data', num2cell(tabela));
            set(handles.uiTabelaIntNum, 'ColumnName',...
                    [{'Exata'}, {'Trapézios'}, {'Erro Trap'}, ...
                    {'Simpson'}, {'Erro Simp'}]);
    end
    
    fplot(f, [a b], 'black');
    legend('f(x)');
    
    h = (b - a) / n;
    t = a: h: b;

    A = area(t, f(t));
    A.FaceColor = [0.80 0 0.2];

    if get(handles.cbFill, 'Value')
        alpha(A, 1);
    else
        alpha(A, 0);
    end

    if get(handles.cbGrid, 'Value')
        grid on;
    else
        grid off;
    end

    if get(handles.cbLegend, 'Value')
        legend('f(x)');
    else
        legend off;
    end

    if get(handles.cbLegendAxes, 'Value')
        xlabel('Eixo dos xx');
        ylabel('Eixo dos yy');
    else
        xlabel('');
        ylabel('');
    end
catch Me
    errordlg(Me.message,'ERRO','modal');
end
end


% --------------------------------------------------------------------
function mExcel_Callback(hObject, eventdata, handles)
% hObject    handle to mExcel (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

filename = inputdlg('Nome do ficheiro:', 'Exportar para Excel', [1 30], {'valores'});
filename = string(filename) + '.xlsx';

l = get(handles.uiTabelaIntNum, 'ColumnName');
l = l.';
tabela = get(handles.uiTabelaIntNum, 'Data');

writecell(tabela, filename, 'Sheet', 1, 'Range', 'A2');
writecell(l, filename, 'Sheet', 1, 'Range', 'A1');
end


% --- Executes when user attempts to close figure1.
function figure1_CloseRequestFcn(hObject, eventdata, handles)
% hObject    handle to figure1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: delete(hObject) closes the figure
set(hObject,'Visible','Off');
end
