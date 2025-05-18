const CACHE_NAME = `temperature-converter-v1`;

// Use the install event to pre-cache all initial resources.
self.addEventListener('install', event => {
  event.waitUntil((async () => {
    const cache = await caches.open(CACHE_NAME);
    cache.addAll([
      '/',
      '/js/categoryregist.js',
      '/js/default.js',
      '/js/check.js',
      '/js/combo.js',
      '/js/dialog.js',
      '/js/form.js',
      '/js/getinfo.js',
      '/js/list.js',
      '/js/recycle.js',
      '/js/sidebar.js',
      '/js/time.js',
      '/js/timeworks.js',
      '/css/item/button.css',
      '/css/item/color.css',
      '/css/item/dialog.css',
      '/css/item/hamburger.css',
      '/css/item/input.css',
      '/css/item/list.css',
      '/css/item/parts.css',
      '/css/item/screen.css',
      '/css/item/select.css',
      '/css/item/style.css',
      '/css/item/table.css',
      '/css/category.css',
      '/css/recyle.css',
      '/css/tablelist.css',
      '/css/timeworks.css'
    ]);
  })());
});

self.addEventListener('fetch', event => {
  event.respondWith((async () => {
    const cache = await caches.open(CACHE_NAME);

    // Get the resource from the cache.
    const cachedResponse = await cache.match(event.request);
    if (cachedResponse) {
      return cachedResponse;
    } else {
        try {
          // If the resource was not in the cache, try the network.
          const fetchResponse = await fetch(event.request);

          // Save the resource in the cache and return it.
          cache.put(event.request, fetchResponse.clone());
          return fetchResponse;
        } catch (e) {
          // The network failed.
        }
    }
  })());
});