function varargout = MaquinaEDO1(varargin)
% MAQUINAEDO1 M-file for MaquinaEDO1.fig
%      MAQUINAEDO1, by itself, creates a new MAQUINAEDO1 or raises the existing
%      singleton*.
%
%      H = MAQUINAEDO1 returns the handle to a new MAQUINAEDO1 or the handle to
%      the existing singleton*.
%
%      MAQUINAEDO1('CALLBACK',hObject,eventData,handles,...) calls the local
%      function named CALLBACK in MAQUINAEDO1.M with the given input arguments.
%
%      MAQUINAEDO1('Property','Value',...) creates a new MAQUINAEDO1 or raises the
%      existing singleton*.  Starting from the left, property value pairs are
%      applied to the GUI before MaquinaEDO1_OpeningFcn gets called.  An
%      unrecognized property name or invalid value makes property application
%      stop.  All inputs are passed to MaquinaEDO1_OpeningFcn via varargin.
%
%      *See GUI Options on GUIDE's Tools menu.  Choose "GUI allows only one
%      instance to run (singleton)".
%
% See also: GUIDE, GUIDATA, GUIHANDLES

% Edit the above text to modify the response to help MaquinaEDO1

% Last Modified by GUIDE v2.5 24-Nov-2017 12:39:56

% Begin initialization code - DO NOT EDIT
gui_Singleton = 1;
gui_State = struct('gui_Name',       mfilename, ...
                   'gui_Singleton',  gui_Singleton, ...
                   'gui_OpeningFcn', @MaquinaEDO1_OpeningFcn, ...
                   'gui_OutputFcn',  @MaquinaEDO1_OutputFcn, ...
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


% --- Executes just before MaquinaEDO1 is made visible.
function MaquinaEDO1_OpeningFcn(hObject, eventdata, handles, varargin)
% This function has no output args, see OutputFcn.
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
% varargin   command line arguments to MaquinaEDO1 (see VARARGIN)

% Choose default command line output for MaquinaEDO1
handles.output = hObject;

% Update handles structure
guidata(hObject, handles);
MyAtualizar(handles);

% UIWAIT makes MaquinaEDO1 wait for user response (see UIRESUME)
% uiwait(handles.figureMaquinaEDO1);


% --- Outputs from this function are returned to the command line.
function varargout = MaquinaEDO1_OutputFcn(hObject, eventdata, handles) 
% varargout  cell array for returning output args (see VARARGOUT);
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Get default command line output from handles structure
varargout{1} = handles.output;


% --- Executes on button press in pbAtualizar.
function pbAtualizar_Callback(hObject, eventdata, handles)
% hObject    handle to pbAtualizar (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
MyAtualizar(handles);


function eF_Callback(hObject, eventdata, handles)
% hObject    handle to eF (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of eF as text
%        str2double(get(hObject,'String')) returns contents of eF as a double


% --- Executes during object creation, after setting all properties.
function eF_CreateFcn(hObject, eventdata, handles)
% hObject    handle to eF (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function eA_Callback(hObject, eventdata, handles)
% hObject    handle to eA (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of eA as text
%        str2double(get(hObject,'String')) returns contents of eA as a double


% --- Executes during object creation, after setting all properties.
function eA_CreateFcn(hObject, eventdata, handles)
% hObject    handle to eA (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function eB_Callback(hObject, eventdata, handles)
% hObject    handle to eB (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of eB as text
%        str2double(get(hObject,'String')) returns contents of eB as a double


% --- Executes during object creation, after setting all properties.
function eB_CreateFcn(hObject, eventdata, handles)
% hObject    handle to eB (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function eN_Callback(hObject, eventdata, handles)
% hObject    handle to eN (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of eN as text
%        str2double(get(hObject,'String')) returns contents of eN as a double


% --- Executes during object creation, after setting all properties.
function eN_CreateFcn(hObject, eventdata, handles)
% hObject    handle to eN (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end



function eY0_Callback(hObject, eventdata, handles)
% hObject    handle to eY0 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of eY0 as text
%        str2double(get(hObject,'String')) returns contents of eY0 as a double


% --- Executes during object creation, after setting all properties.
function eY0_CreateFcn(hObject, eventdata, handles)
% hObject    handle to eY0 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end

function MyAtualizar(handles)
strF = get(handles.eF, 'String');
f = @(t,y) eval(vectorize(strF));

a = str2num(get(handles.eA, 'String'));
b = str2num(get(handles.eB, 'String'));
n = str2num(get(handles.eN, 'String'));
y0 = str2num(get(handles.eY0, 'String'));

escolhabg = get(handles.bgMetodos, 'SelectedObject');
escolha = find([handles.rbEuler,...
              handles.rbEulerM,...
              handles.rbRK2,...
              handles.rbRK4,...
              handles.rbODE45,...
              handles.rbTodos]==escolhabg);
EULER=1;
EULERM=2;
RK2=3;
RK4=4;
ODE_45=5;
TODOS=6;
    
testeFunc=MException('MATLAB:MyAtualizar:badFunc',....
                     'Introduza uma função em t e y');
testeNReal=MException('MATLAB:MyAtualizar:badNReal',....
                     'Introduza um número real');
% outras                 
                 
try  
    try
        fTeste=f(sym('t'),sym('y'));  
    catch
        throw(testeFunc);
    end
    
    if ~(isscalar(a) && isreal(b)) throw(testeNReal); end
    
    % outras validações
    
    
   
    
    yEuler=N_Euler(f,a,b,n,y0);
    yRK2=N_RK2(f,a,b,n,y0);

    sExata=dsolve(['Dy=',strF],...
          ['y(',num2str(a),')=',num2str(y0)]);
    t=a:(b-a)/n:b;
    yExata=eval(vectorize(char(sExata)));

    erroEuler=abs(yExata-yEuler);
    erroRK2=abs(yExata-yRK2);
    
    plot(t,yExata);
    hold on  
    
    switch escolha
        case EULER
              plot(t,yEuler,'r');
              legend('yExata','yEuler');
              tabela=[t.',yExata.',yEuler.',erroEuler.']; 
              set(handles.uiTabela,'Data',num2cell(tabela)); 
              set(handles.uiTabela,'ColumnName',...
                  [{'t'},{'Exata'},{'Euler'},{'ErroEuler'}]);
        case EULERM
            warndlg('Método não implementado','AVISO','modal');
        case RK2
              plot(t,yRK2,'g');
              legend('yExata','yRK2');
              tabela=[t.',yExata.',yRK2.',erroRK2.']; 
              set(handles.uiTabela,'Data',num2cell(tabela)); 
              set(handles.uiTabela,'ColumnName',...
                  [{'t'},{'Exata'},{'RK2'},{'ErroRK2'}]);
        case RK4
            warndlg('Método não implementado','AVISO','modal');
        case ODE_45
            warndlg('Método não implementado','AVISO','modal');
        case TODOS
             plot(t,yEuler,'r');
             plot(t,yRK2,'g');
             legend('yExata','yEuler','yRK2');
  
            tabela=[t.',yExata.',yEuler.',yRK2.',...
                      erroEuler.',erroRK2.'];
            set(handles.uiTabela,'Data',num2cell(tabela));  
    end
    hold off
    grid on
catch Me
    errordlg(Me.message,'ERRO','modal')
end


% --------------------------------------------------------------------
function MFile_Callback(hObject, eventdata, handles)
% hObject    handle to MFile (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)


% --------------------------------------------------------------------
function MSair_Callback(hObject, eventdata, handles)
% hObject    handle to MSair (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
SN=questdlg('Quer mesmo sair?','SAIR','Sim','Não','Sim');
if strcmp(SN,'Não')
    return;
end
delete(handles.figureMaquinaEDO1);


% --- Executes when user attempts to close figureMaquinaEDO1.
function figureMaquinaEDO1_CloseRequestFcn(hObject, eventdata, handles)
% hObject    handle to figureMaquinaEDO1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: delete(hObject) closes the figure
%delete(hObject);
MSair_Callback([], [], handles);
