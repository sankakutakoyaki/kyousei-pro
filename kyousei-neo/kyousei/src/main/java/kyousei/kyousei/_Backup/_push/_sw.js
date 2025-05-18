const CACHE_NAME = `temperature-converter-v1`;

// Use the install event to pre-cache all initial resources.
self.addEventListener('install', event => {
  event.waitUntil((async () => {
    const cache = await caches.open(CACHE_NAME);
    cache.addAll([
      '/',
      '/js/cashexpenses.js',
      '/js/default.js',
      '/js/recycle.js',
      '/js/tablelist.js',
      '/js/timeworks.js',
      '/css/item/button.css',
      '/css/item/color.css',
      '/css/item/default.css',
      '/css/item/dialog.css',
      '/css/item/input.css',
      '/css/item/list.css',
      '/css/item/tab.css',
      '/css/item/table.css',
      '/css/index.css',
      '/css/tablelist.css'
    ]);
  })());
});

// self.addEventListener('fetch', event => {
//   event.respondWith((async () => {
//     const cache = await caches.open(CACHE_NAME);

//     // Get the resource from the cache.
//     const cachedResponse = await cache.match(event.request);
//     if (cachedResponse) {
//       return cachedResponse;
//     } else {
//       try {
//         // If the resource was not in the cache, try the network.
//         const fetchResponse = await fetch(event.request);

//         // Save the resource in the cache and return it.
//         cache.put(event.request, fetchResponse.clone());
//         return fetchResponse;
//       } catch (e) {
//         // The network failed.
//       }
//     }
//   })());
// });

// self.addEventListener('push', function(event) {
//   const data = event.data.json();
//   const title = data.title || 'New Notification';
//   const options = {
//     body: data.body || 'You have a new message!',
//     icon: data.icon || '/icon.png',
//     badge: data.badge || '/badge.png'
//   };

//   event.waitUntil(
//     self.registration.showNotification(title, options)
//   );
// });

// self.addEventListener('push', function (event) {
//   var options = {
//       body: event.data.text(),
//       icon: '/icon.png',
//       badge: '/badge.png'
//   };

//   event.waitUntil(
//       self.registration.showNotification('プッシュ通知', options)
//   );
// });

self.addEventListener('push', function(event) {
  let notificationData = event.data.json();

  const options = {
      body: notificationData.body,
      icon: 'icon.png',
      badge: 'badge.png'
  };

  event.waitUntil(
      self.registration.showNotification(notificationData.title, options)
  );
});

self.addEventListener('notificationclick', function(event) {
  event.notification.close();
  event.waitUntil(
      clients.openWindow('https://www.example.com')
  );
});


// self.addEventListener("push", function (event) {
//   const data = event.data ? event.data.text() : "No payload";
//   event.waitUntil(
//       self.registration.showNotification("プッシュ通知", {
//           body: data,
//           icon: "/icon.png",
//       })
//   );
// });

// self.addEventListener("push", event => {
//   const options = {
//       body: event.data ? event.data.text() : "新しい通知があります！",
//       icon: "/icon.png"
//   };

//   event.waitUntil(
//       self.registration.showNotification("プッシュ通知", options)
//   );
// });
// self.addEventListener('push', function (event) {
//   console.log('sw event: push called');

//   const notificationDataObj = event.data.json();
//   const content = {
//     body: notificationDataObj.body,
//   };
//   event.waitUntil(
//     self.registration.showNotification(notificationDataObj.title, content)
//   );
// });

// self.addEventListener("push", function(event) {
//   console.log("Push Notification Recieved", event);
//   if (Notification.permission == "granted") {
//     event.waitUntil(
//       self.registration
//         .showNotification("受信しました", {
//           body: "お知らせです。",
//           icon: "iconV2.png"
//         })
//         .then(
//           function(showEvent) {},
//           function(error) {
//             console.log(error);
//           }
//         )
//     );
//   }
// });

// Notification.requestPermission(function(status) {
//   console.log("通知の許可:", status);//コンソールに許可されたかどうかを表示
// });

// self.addEventListener("notificationclick", function(event) {
//   event.notification.close();
//   event.waitUntil(
//     clients.openWindow("https://watanabe0601.github.io/sw.github.io/02/")
//   );
// });