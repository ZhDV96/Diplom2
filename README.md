Дипломная работа, вторая часть 
====
## Описание проекта
Вторая часть дипломной работы курса "Автоматизатор тестирования на Java" выполнена для тестирования API учебного приложения [Stellar Burgers](https://practicum.yandex.ru/learn/qa-automation-engineer-java/courses/f2900d41-051b-4ca3-b522-edd0143c8705/sprints/34751/topics/670c9352-0715-4f79-ad00-871a53410c9a/lessons/369c666b-acfe-430e-a905-c4936d3a0c2c/#:~:text=%D0%BF%D0%BE%D0%BA%D1%80%D1%8B%D1%82%D1%8C%20%D1%82%D0%B5%D1%81%D1%82%D0%B0%D0%BC%D0%B8%20%D0%BF%D1%80%D0%B8%D0%BB%D0%BE%D0%B6%D0%B5%D0%BD%D0%B8%D0%B5-,Stellar%20Burgers,-.%20%D0%AD%D1%82%D0%BE%20%D0%BA%D0%BE%D1%81%D0%BC%D0%B8%D1%87%D0%B5%D1%81%D0%BA%D0%B8%D0%B9%20%D1%84%D0%B0%D1%81%D1%82%D1%84%D1%83%D0%B4). В проекте реализованы автотесты для API учебного сервиса Яндекс.Самокат. Его документация: qa-scooter.praktikum-services.ru/docs/. Тесты написаны на "ручки" разделов "Courier", "Couriers", "Orders", проверяют корректную работу API для GET, DELETE, POST -запросов.

## Перед началом работы
Необходимо, чтобы были уставновлены:
•	IntelliJ IDEA + Maven 
•	Git Bash
### Переключитесь на ветку develop.
### Склонировать проект и запустить его локально
1.	Перейдите в репозиторий с заготовкой кода: https://github.com/ZhDV96/Sprint_3.git
2.	Нажмите кнопку Fork для клонирования репозитория — она на панели справа и сверху. Появится выпадающий список. Выберите свой аккаунт на GitHub.
3.	Откройте терминал(Git Bash) и перейдите в папку проекта, которую удалось создать на компьютере. Для этого понадобится команда cd.
4.	Используя команду git clone https://github.com/<username>/Sprint_3.git в терминале, скачайте себе данный репозиторий. Укажите имя своего аккаунта на GitHub вместо <username>.
### Отчёт с Allure.
1.	В IntelliJ IDEA с подключенным Maven запустите выполнение автотестов командой mvn clean test.
2.	В консоли IntelliJ IDEA перейдите в папку проекта и выполните команду allure serve target/surefire-reports/. Allure отобразит отчёт, который сгенерировал плагин maven-surefire-plugin. Откроется окно браузера с отчётом.
