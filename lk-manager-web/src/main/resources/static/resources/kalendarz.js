

let calendar = document.querySelector('.calendar')

const month_names = ['Styczeń', 'Luty', 'Marzec', 'Kwiecień', 'Maj', 'Czerwiec', 'Lipiec', 'Sierpień', 'Wrzesień', 'Październik', 'Listopad', 'Grudzień']

isLeapYear = (year) => {
    return (year % 4 === 0 && year % 100 !== 0 && year % 400 !== 0) || (year % 100 === 0 && year % 400 ===0)
}

getFebDays = (year) => {
    return isLeapYear(year) ? 29 : 28
}

generateCalendar = (month, year) => {

    let calendar_days = calendar.querySelector('.calendar-days')
    let calendar_header_year = calendar.querySelector('#year')

    let days_of_month = [31, getFebDays(year), 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]

    calendar_days.innerHTML = ''

    let currDate = new Date()
    if (month > 11 || month < 0) month = currDate.getMonth()
    if (!year) year = currDate.getFullYear()

    let curr_month = `${month_names[month]}`
    month_picker.innerHTML = curr_month
    calendar_header_year.innerHTML = year

    // get first day of month

    let first_day = new Date(year, month, 0)

    for (let i = 0; i <= days_of_month[month] + first_day.getDay()-1 ; i++) {
        let day = document.createElement('div')


        if (i >= first_day.getDay()) {
            day.classList.add('calendar-day-hover')




day.onclick = function()
                  {
              /*     alert(i - first_day.getDay() + 1);*/
       //           console.log(document.getElementById("labelData").innerText);


  let pickedDate = new Date()
pickedDate.setDate(i-1 );
pickedDate.setMonth(month);
pickedDate.setYear(year);
//if(currDate)
//alert(pickedDate+"   "+currDate)
if(pickedDate<currDate)
{alert("Data rozpoczęcia nie może być wcześniejsza od dzisiaj")
}
else
{


let labelData= document.getElementById("labelData");
let inputData= document.getElementById("inputData");
labelData.innerHTML="  "+year+"-"+(month+1)+"-"+ (i - first_day.getDay() + 1);
inputData.value="  "+year+"-"+(month+1)+"-"+ (i - first_day.getDay() + 1);
  console.log(document.getElementById("labelData").innerText);

    console.log(labelData.value);
      console.log(inputData.value);

let calendar_days1 = document.getElementById("sam");

 /* calendar_days1.children.forEach(element => element.classList.add('select'));*/
let month_list = calendar.querySelector('.calendar-day-hover')

/*month_list.children.forEach(element => element.classList.remove('select'));
month_list.classList.remove('select')*/
//day.classList.add('select')


var t= document.getElementsByClassName('select');
/*
t.forEach(
a=>{a.classList.remove('select')
console.log( "kk"+a)
}
)
*/
  console.log("a"+t)
for (var ii = 0; ii < t.length; ii++) {
t[ii].classList.remove('select')

   //   console.log("b"+t[ii].innerHTML);
  }

day.classList.add('select')




}


                  }



            day.innerHTML = i - first_day.getDay() + 1
            day.innerHTML += `<span></span>
                            <span></span>
                            <span></span>
                            <span></span>`
            if (i - first_day.getDay() + 1 === currDate.getDate() && year === currDate.getFullYear() && month === currDate.getMonth()) {
                day.classList.add('curr-date')
            }
        }
        calendar_days.appendChild(day)
    }
}




let month_list = calendar.querySelector('.month-list')

month_names.forEach((e, index) => {
    let month = document.createElement('div')
    month.innerHTML = `<div data-month="${index}">${e}</div>`
    month.querySelector('div').onclick = () => {
        month_list.classList.remove('show')
        curr_month.value = index
        generateCalendar(index, curr_year.value)
    }
    month_list.appendChild(month)
})








let month_picker = calendar.querySelector('#month-picker')

month_picker.onclick = () => {
    month_list.classList.add('show')
}

let currDate = new Date()

let curr_month = {value: currDate.getMonth()}
let curr_year = {value: currDate.getFullYear()}

generateCalendar(curr_month.value, curr_year.value)

document.querySelector('#prev-year').onclick = () => {
    --curr_year.value
    generateCalendar(curr_month.value, curr_year.value)
}

document.querySelector('#next-year').onclick = () => {
    ++curr_year.value
    generateCalendar(curr_month.value, curr_year.value)
}












