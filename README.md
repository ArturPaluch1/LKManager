# LKManager



Program wykonany z użyciem Spring Boot, umożliwia tworzenie i łatwe zarządzanie rozgrywkami klanowymi opartymi na meczach towarzyskich w managerze internetowym <a href ="https://www.managerzone.com/">Managerzone</a>.
<details >
 <summary>Managerzone</summary>
<a href="https://ibb.co/Fw73RyL"><img src="https://i.ibb.co/fp0Fjyz/mz.png" alt="mz" border="0" /></a>
</details>
Główna baza danych (SQL) znajduje sę na serwerze. Dodatkowo, żeby usprawnić działanie programu (zapytania sql na serwerze produkcyjnym realizowane są z lekkim opóźnieniem) część najczęściej używanych danych (lista użytkowników, tabela, terminarz) są zapisywane także w bazie danych no-sql Redis. Ostatnio odwiedzony terminarz i runda zapisywane są w plikach cookie, aby w razie większej ilości terminarzy użytkownik nie był pogubiony.
Rozgrywki odbywają się co wtorek. Codziennie program sprawdza, czy jest zaplanowana kolejny terminarz ligowy, jeśli nie to go tworzy. Sprawdza czy obecna liga dobiegłą końca, jeśli tak to zaplanowany terminarz uzupełnia rundami stworzonymi dla graczy którzy wcześniej się zapisali się do gry. We wtorek o 10:55, 11:55, 19:55, 20:55 i 23:55 podejmowane są próby pobrania wyników meczów z MZ (api Managerzone pozwala pobierać wyniki w formacie XML), następnie uzupełniana jest runda i zapisywana w bazie danych. Przy każdej aktualizacji wyników tworzona jest też aktualna tabela i zapisywana w bazie Redis. Użytkownicy mogą je przeglądać na stronie HTML. 

Serwis jest ze względu na 3 rodzaje użytkowników. Niezalogowanych, zalogowanych i admina.

<details >
 <summary>Niezalogowany użytkownik może przeglądać stronę główną:</summary>
<a href="https://ibb.co/T0Cr9CZ"><img src="https://i.ibb.co/M1X9FXb/Screenshot-2024-10-17-at-00-01-05-LK-Manager.png" alt="Screenshot-2024-10-17-at-00-01-05-LK-Manager" border="0"></a>

</details>
<details >
 <summary>Przeglądać tabele:
</summary>
<a href="https://ibb.co/51tQdsf"><img src="https://i.ibb.co/6yVK2Df/Screenshot-2024-10-17-at-00-02-23-Title.png" alt="Screenshot-2024-10-17-at-00-02-23-Title" border="0"></a>

</details>
<details >
 <summary>I wyniki:
</summary>
<a href="https://ibb.co/T4rX2wd"><img src="https://i.ibb.co/BywD3fF/Screenshot-2024-10-17-at-00-02-37-Navbar.png" alt="Screenshot-2024-10-17-at-00-02-37-Navbar" border="0"></a>

</details>
<details >
 <summary>A także założyć konto:
</summary>
<a href="https://ibb.co/JH16rkN"><img src="https://i.ibb.co/QMgSYmZ/Screenshot-2024-10-17-at-00-02-49-Sign-up.png" alt="Screenshot-2024-10-17-at-00-02-49-Sign-up" border="0"></a>

</details>

<details >
 <summary>Albo się zalogować:
</summary>
<a href="https://ibb.co/ZgX7pYP"><img src="https://i.ibb.co/bvmDM2Z/Screenshot-2024-10-17-at-00-02-59-Log-in.png" alt="Screenshot-2024-10-17-at-00-02-59-Log-in" border="0"></a>

</details>

<details >
 <summary>Zalogowany użytkownik ma dodatkowo możliwość ustawić swoje konto:
</summary>


</details>
<details >
 <summary>Ustawić nazwę swojego konta w MZ, które będzie uwzględniane do tworzenia terminarzy:
</summary>


</details>
Po dodaniu konta MZ użytkownik dostaje możliwość dołączenia do przyszłej ligi, ją zasubskrybować do odwołania, albo się wypisać.

<details >
 <summary>Ustawić email, żeby móc przypomnieć swoje hasło (jeszcze niedostępna opcja). Po wpisaniu emaila dostaje mail z linkiem potwierdzającym prawdziwość maila.
</summary>


</details>

<details >
 <summary>Admin poza opcjami zalogowanego użytkownika może przeglądać zarejestrowanych użytkowników, dodawać nowych (głównie było wykorzystywane do tworzenia użytkowników tekstowych zanim poja wiła się opcja tworzena konta) i usuwać.
</summary>


</details>

<details >
 <summary>W zakładce wyniki może bezpośrednio (poza zaprogramowanym automatycznym zbieraniem wyników o konkretnych godzinach) aktualizować wyniki. 
</summary>


</details>
<details >
 <summary>A także je edytować.
</summary>


</details>
<details >
 <summary>Zakłądka terminarz pozwala przeglądać stworzone terminarze.
</summary>


</details>
<details >
 <summary>Usuwać je:
</summary>


</details>
<details >
 <summary>I dodawać nowe - 3 rodzaje, jednodniowy:
</summary>


</details>
<details >
 <summary>Wielodniowy:
</summary>


</details>
Ta opcja tworzy terminarz uwzględniający wszystkich wskazanych graczy 

<details >
 <summary>W systemie szwajcarskim:
</summary>


</details>
W tym rodzaju tworzona jest tylko pierwsza runda rozgrywek, a kolejne są tworzone zgodnie z harmonogramem dzień po rozegraniu poprzedniej kolejki. Kiedy dzień meczowy mija program oblicza na podstawie sum punktów graczy z wcześniej rozegranych kolejek tabelę, a następnie tworzy pary na następny tydzień. Gracz z największą liczbą punktów gra z kolejnym, i tak dalej, przy czym jeśli dana para już wystąpła, to dobierana jest para z większą różnicą punktów między graczami.





Użytkownik w zakładce "Gracze" dodaje i usuwa graczy, automatycznie weryfikując poprawność nicku.
<details >
 <summary>Gracze</summary>
<a href="https://ibb.co/wYFYFHn"><img src="https://i.ibb.co/TrxrxXQ/gracze.png" alt="gracze" border="0"></a>

</details>
Następnie w zakładce "Terminarz" tworzy termiarz. Terminarz może być "jednodniowy" lub "wielodniowy". 
W przypadku terminarza jednodniowego użytkownik musi wybrać datę meczów i nazwę, a także wybierjąc kolejne nicki graczy i akceptując ustalić kolejne mecze. 
<details >
 <summary>Terminarz jednodniowy</summary>
<a href="https://ibb.co/dM4pm97"><img src="https://i.ibb.co/2h3MZ25/schedule-dodaj1dniowy.png" alt="schedule-dodaj1dniowy" border="0"></a>

</details>
Terminarz "wielodniowy" wymaga jedynie ustalenia nazwy i początkowej daty rozgrywek, a także wybranie wszystkich uczestniczących graczy. Terminarz na kolejne cotygodniowe mecze jest tworzony automatycznie.
<details >
 <summary>Terminarz wielodniowy</summary>
<a href="https://ibb.co/pvtDCzQ"><img src="https://i.ibb.co/ccG5zFw/schedule-dodajwielodniowy.png" alt="schedule-dodajwielodniowy" border="0"></a>

</details>
Również w tej zakładce użytkownik może usuwać i przeglądać terminarze, poszczególne kolejki.

<details >
 <summary>Terminarz</summary>
<a href="https://ibb.co/2qYdBXg"><img src="https://i.ibb.co/H7pqQmn/schedule.png" alt="schedule" border="0"></a>

</details>
<details >
 <summary>Terminarze - zmiana rundy</summary>
<a href="https://ibb.co/QcNvrcg"><img src="https://i.ibb.co/j5Mkg5N/schedule-zmiana-rundy.png" alt="schedule-zmiana-rundy" border="0"></a>

</details>
A także kopiować schedule danej kolejki do schowka w celu wklejenia par na forum klanu.
<details >
 <summary>Kopiowanie do schowka</summary>
<a href="https://ibb.co/m4n8Xdh"><img src="https://i.ibb.co/DQBp8mw/kopiowanie-do-schowka.png" alt="kopiowanie-do-schowka" border="0"></a>

</details>
Po rozegraniu meczy w Managerzone użytkownik przyciskiem Aktualizuj wyniki może pobrać wyniki danej rundy bezpośrednio ze strony gry w postaci xml. 
<details>
 <summary>Wyniki puste</summary>
<a href="https://ibb.co/XbDwgd2"><img src="https://i.ibb.co/Cz6rZX1/wyniki-puste.png" alt="wyniki-puste" border="0"></a>

</details>


<details>
 <summary>Wyniki zaktualizowane</summary>
<a href="https://ibb.co/zXyVVMF"><img src="https://i.ibb.co/bsS550F/wyniki-uzupeln.png" alt="wyniki-uzupeln" border="0"></a>

</details>
Może też edytować wyniki np. w celu wpisania walkowerów.
<details >
 <summary>Edycja wyników</summary>
<a href="https://ibb.co/BZqtrt6"><img src="https://i.ibb.co/2tgysyM/wyniki-edycja.png" alt="wyniki-edycja" border="0"></a>

</details>
W zakładce "Tabela" użytkownik może wyświetlać tabelę obliczaną na podstawie danych zapisanych w plikach xml terminarza.
<details >
 <summary>Tabela</summary>
<a href="https://ibb.co/s3JQ9Fz"><img src="https://i.ibb.co/pXrZj35/table.png" alt="table" border="0"></a>

</details>
