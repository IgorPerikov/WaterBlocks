var xhttp;
if (window.XMLHttpRequest) {
    xhttp = new XMLHttpRequest();
} else {
    xhttp = new ActiveXObject("Microsoft.XMLHTTP");
}

var href = window.location.href;
var id = href.substring(href.lastIndexOf("=") + 1, href.length);
var grid;

xhttp.onreadystatechange = function() {
    if (xhttp.readyState == 4 && xhttp.status == 200) {
        var resultString = xhttp.response;
        var result = JSON.parse(resultString);
        var sequenceArray = result["sequence"].split(" ");
        var arraySize = sequenceArray.length;
        grid = new Array(arraySize);
        for (var i = 0; i < arraySize; i++) {
            grid[i] = new Array(arraySize);
        }
        for (var i = 0; i < arraySize; i++) {
            for (var j = 0; j < arraySize; j++) {
                if (arraySize - i > sequenceArray[j]) {
                    grid[i][j] = 0;
                } else {
                    grid[i][j] = 1;
                }
            }
        }

        var count = 0;
        for (var k = 0; k < result.waterAreas.length; k++) {
            var border = Math.min(sequenceArray[result.waterAreas[k].leftBorder], sequenceArray[result.waterAreas[k].rightBorder]);
            for (var i = result.waterAreas[k].leftBorder; i < result.waterAreas[k].rightBorder; i++) {
                if (sequenceArray[i] < border) {
                    for (var j = 0; j < arraySize; j++) {
                        if (arraySize - j <= border) {
                            if (grid[j][i] == 0) {
                                count++;
                                grid[j][i] = 2;
                            }
                        }
                    }
                }
            }
        }
        for (var i = 0; i < grid.length; i++) {
            var flag = false;
            for (var j = 0; j < grid.length; j++) {
                if (grid[i][j] != 0) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                grid = grid.slice(i, grid.length);
                break;
            }
        }

        webGLStart();
    }
};
var url = "raw/results/" + id;
xhttp.open("GET", url, true);
xhttp.send();

var gl;
function initGL(canvas) {
    try {
        gl = canvas.getContext("webgl");
        gl.viewportWidth = canvas.width;
        gl.viewportHeight = canvas.height;
    } catch (e) {
    }
    if (!gl) {
        alert("your browser doesn't support webgl");
    }
}
function getShader(gl, id) {
    var shaderScript = document.getElementById(id);
    if (!shaderScript) {
        return null;
    }
    var str = "";
    var k = shaderScript.firstChild;
    while (k) {
        if (k.nodeType == 3) {
            str += k.textContent;
        }
        k = k.nextSibling;
    }
    var shader;
    if (shaderScript.type == "x-shader/x-fragment") {
        shader = gl.createShader(gl.FRAGMENT_SHADER);
    } else if (shaderScript.type == "x-shader/x-vertex") {
        shader = gl.createShader(gl.VERTEX_SHADER);
    } else {
        return null;
    }
    gl.shaderSource(shader, str);
    gl.compileShader(shader);
    if (!gl.getShaderParameter(shader, gl.COMPILE_STATUS)) {
        alert(gl.getShaderInfoLog(shader));
        return null;
    }
    return shader;
}
var shaderProgram;
function initShaders() {
    var fragmentShader = getShader(gl, "shader-fs");
    var vertexShader = getShader(gl, "shader-vs");
    shaderProgram = gl.createProgram();
    gl.attachShader(shaderProgram, vertexShader);
    gl.attachShader(shaderProgram, fragmentShader);
    gl.linkProgram(shaderProgram);
    if (!gl.getProgramParameter(shaderProgram, gl.LINK_STATUS)) {
        alert("could not initialise shaders");
    }
    gl.useProgram(shaderProgram);
    shaderProgram.vertexPositionAttribute = gl.getAttribLocation(shaderProgram, "aVertexPosition");
    gl.enableVertexAttribArray(shaderProgram.vertexPositionAttribute);
    shaderProgram.vertexColorAttribute = gl.getAttribLocation(shaderProgram, "aVertexColor");
    gl.enableVertexAttribArray(shaderProgram.vertexColorAttribute);
    shaderProgram.pMatrixUniform = gl.getUniformLocation(shaderProgram, "uPMatrix");
    shaderProgram.mvMatrixUniform = gl.getUniformLocation(shaderProgram, "uMVMatrix");
}

var mvMatrix = mat4.create();
var pMatrix = mat4.create();
function setMatrixUniforms() {
    gl.uniformMatrix4fv(shaderProgram.pMatrixUniform, false, pMatrix);
    gl.uniformMatrix4fv(shaderProgram.mvMatrixUniform, false, mvMatrix);
}

var squareVertexPositionBuffer;
var emptyBlockColorBuffer;
var waterBlockColorBuffer;
var wallBlockColorBuffer;

function initBuffers() {
    squareVertexPositionBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, squareVertexPositionBuffer);
    vertices = [
        1.0,  1.0,  0.0,
        -1.0,  1.0,  0.0,
        1.0, -1.0,  0.0,
        -1.0, -1.0,  0.0
    ];
    gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertices), gl.STATIC_DRAW);
    squareVertexPositionBuffer.itemSize = 3;
    squareVertexPositionBuffer.numItems = 4;


    emptyBlockColorBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, emptyBlockColorBuffer);
    colors = [];
    for (var i = 0; i < 4; i++) {
        colors = colors.concat([0.85, 0.85, 0.85, 1.0]);
    }
    gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(colors), gl.STATIC_DRAW);
    emptyBlockColorBuffer.itemSize = 4;
    emptyBlockColorBuffer.numItems = 4;


    waterBlockColorBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, waterBlockColorBuffer);
    colors = [];
    for (var i = 0; i < 4; i++) {
        colors = colors.concat([0.5, 0.5, 1.0, 1.0]);
    }
    gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(colors), gl.STATIC_DRAW);
    waterBlockColorBuffer.itemSize = 4;
    waterBlockColorBuffer.numItems = 4;


    wallBlockColorBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, wallBlockColorBuffer);
    colors = [];
    for (var i = 0; i < 4; i++) {
        colors = colors.concat([0.1, 0.1, 0.1, 1.0]);
    }
    gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(colors), gl.STATIC_DRAW);
    wallBlockColorBuffer.itemSize = 4;
    wallBlockColorBuffer.numItems = 4;
}

function drawScene() {
    var size = 2.05;

    gl.viewport(0, 0, gl.viewportWidth, gl.viewportHeight);
    gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);
    mat4.perspective(45, gl.viewportWidth / gl.viewportHeight, 0.1, 100.0, pMatrix);
    mat4.identity(mvMatrix);
    mat4.translate(mvMatrix, [-50, 35, -95.0]);

    for (var i = 0; i < grid.length; i++) {
        for (var j = 0; j < grid[i].length; j++) {
            mat4.translate(mvMatrix, [size, 0.0, 0.0]);
            gl.bindBuffer(gl.ARRAY_BUFFER, squareVertexPositionBuffer);
            gl.vertexAttribPointer(shaderProgram.vertexPositionAttribute, squareVertexPositionBuffer.itemSize, gl.FLOAT, false, 0, 0);

            switch (grid[i][j]) {
                case 0:
                    gl.bindBuffer(gl.ARRAY_BUFFER, emptyBlockColorBuffer);
                    gl.vertexAttribPointer(shaderProgram.vertexColorAttribute, emptyBlockColorBuffer.itemSize, gl.FLOAT, false, 0, 0);
                    break;
                case 1:
                    gl.bindBuffer(gl.ARRAY_BUFFER, wallBlockColorBuffer);
                    gl.vertexAttribPointer(shaderProgram.vertexColorAttribute, wallBlockColorBuffer.itemSize, gl.FLOAT, false, 0, 0);
                    break;
                case 2:
                    gl.bindBuffer(gl.ARRAY_BUFFER, waterBlockColorBuffer);
                    gl.vertexAttribPointer(shaderProgram.vertexColorAttribute, waterBlockColorBuffer.itemSize, gl.FLOAT, false, 0, 0);
                    break;
            }
            setMatrixUniforms();
            gl.drawArrays(gl.TRIANGLE_STRIP, 0, squareVertexPositionBuffer.numItems);

        }
        mat4.translate(mvMatrix, [-size * grid[i].length, -size, 0.0]);
    }
}

function webGLStart() {
    var canvas = document.getElementById("canvas");
    initGL(canvas);
    initShaders();
    initBuffers();
    gl.clearColor(0.85, 0.85, 0.85, 1.0);
    gl.enable(gl.DEPTH_TEST);
    drawScene();
}