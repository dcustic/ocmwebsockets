<template>
  <div id="app">
    <WebSocket />
  </div>
</template>

<script>
  import WebSocket from './components/WebSocket.vue';

export default {
  name: 'app',
  components: {
    WebSocket
  }
}
</script>