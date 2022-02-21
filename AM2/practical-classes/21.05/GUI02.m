function varargout = GUI02(varargin)
% FR2VR - Função real de duas variáveis reais
% Comunicação entre duas GUI
% 2ª parte de programa de AM2
% Licenciaturas em Eng. Informática
% 10ª semana de aulas
% 5/04/2017    Arménio Correia | armenioc@isec.pt
%
% GUI02 M-file for GUI02.fig
%      GUI02, by itself, creates a new GUI02 or raises the existing
%      singleton*.
%
%      H = GUI02 returns the handle to a new GUI02 or the handle to
%      the existing singleton*.
%
%      GUI02('CALLBACK',hObject,eventData,handles,...) calls the local
%      function named CALLBACK in GUI02.M with the given input arguments.
%
%      GUI02('Property','Value',...) creates a new GUI02 or raises the
%      existing singleton*.  Starting from the left, property value pairs are
%      applied to the GUI before GUI02_OpeningFcn gets called.  An
%      unrecognized property name or invalid value makes property application
%      stop.  All inputs are passed to GUI02_OpeningFcn via varargin.
%
%      *See GUI Options on GUIDE's Tools menu.  Choose "GUI allows only one
%      instance to run (singleton)".
%
% See also: GUIDE, GUIDATA, GUIHANDLES

% Edit the above text to modify the response to help GUI02

% Last Modified by GUIDE v2.5 05-May-2017 12:59:36

% Begin initialization code - DO NOT EDIT
gui_Singleton = 1;
gui_State = struct('gui_Name',       mfilename, ...
                   'gui_Singleton',  gui_Singleton, ...
                   'gui_OpeningFcn', @GUI02_OpeningFcn, ...
                   'gui_OutputFcn',  @GUI02_OutputFcn, ...
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


% --- Executes just before GUI02 is made visible.
function GUI02_OpeningFcn(hObject, eventdata, handles, varargin)
% This function has no output args, see OutputFcn.
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
% varargin   command line arguments to GUI02 (see VARARGIN)

% Choose default command line output for GUI02
handles.output = hObject;

% Update handles structure
guidata(hObject, handles);

% UIWAIT makes GUI02 wait for user response (see UIRESUME)
% uiwait(handles.figureGUI02);


% --- Outputs from this function are returned to the command line.
function varargout = GUI02_OutputFcn(hObject, eventdata, handles) 
% varargout  cell array for returning output args (see VARARGOUT);
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Get default command line output from handles structure
varargout{1} = handles.output;


% --- Executes on button press in pbRFuncao.
function pbRFuncao_Callback(hObject, eventdata, handles)
% hObject    handle to pbRFuncao (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
hGUI01 = findobj('Tag', 'figureGUI01');

if ~isempty(hGUI01)
    GUI01Dados = guidata(hGUI01);
    strF = get(GUI01Dados.eF, 'String');
    
    f = @(x, y) eval(vectorize(strF));
    
    a = str2num(get(GUI01Dados.eA, 'String'));
    h1 = str2num(get(GUI01Dados.eH1, 'String'));
    b = str2num(get(GUI01Dados.eB, 'String'));
    c = str2num(get(GUI01Dados.eC, 'String'));
    h2 = str2num(get(GUI01Dados.eH2, 'String'));
    d = str2num(get(GUI01Dados.eD, 'String'));
    
    [x, y] = meshgrid(a: h1: b, c: h2: d);
    z = f(x, y);
    axes(handles.axesGrafico3D);
    surf(x, y, z);
    
    axes(handles.axesGraficoCNivel);
    contour(x, y, z);
end

% --- Executes on button press in pbOFuncao.
function pbOFuncao_Callback(hObject, eventdata, handles)
% hObject    handle to pbOFuncao (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
GUI01();


% --- Executes on button press in pbSairGUI02.
function pbSairGUI02_Callback(hObject, eventdata, handles)
% hObject    handle to pbSairGUI02 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
delete(handles.figureGUI02);


% --- Executes on button press in cbGrelha.
function cbGrelha_Callback(hObject, eventdata, handles)
% hObject    handle to cbGrelha (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of cbGrelha
axes(handles.axesGraficoCNivel);

if get(hObject, 'Value')
    grid on;
else
    grid off;
end


% --- Executes on button press in cbMarcarP.
function cbMarcarP_Callback(hObject, eventdata, handles)
% hObject    handle to cbMarcarP (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hint: get(hObject,'Value') returns toggle state of cbMarcarP
axes(handles.axesGraficoCNivel);

if get(hObject, 'Value')
    [x, y] = ginput(1);
    gtext({['(', num2str(x), ' ,', num2str(y), ')']});
end
