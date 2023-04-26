function pokazEdytujTabele()
{
var edytujTabele=document.getElementById("edytujTabela");
var tabela=document.getElementById("tabela");

tabela.hidden=true;

edytujTabele.hidden=false;
}

function anuluj(){

var edytujTabele=document.getElementById("edytujTabela");
var tabela=document.getElementById("tabela");



edytujTabele.hidden=true;
tabela.hidden=false;



}



function zapiszZmiany()
{
var edytujTabele=document.getElementById("edytujTabela");
var tabela=document.getElementById("tabela");

//tabela.hidden=false;

//edytujTabele.hidden=true;

 alert(tabela);

 var zawartosc =document.getElementById("edytujTabela-zawartosc");
alert(zawartosc.children)

//0 team1 1 u1 9 u2 10t2
 console.log(zawartosc.children[0].children)
 alert(zawartosc);


var edytujTabelaForm=document.getElementById("edytujTabelaForm");
edytujTabelaForm.submit();

}
