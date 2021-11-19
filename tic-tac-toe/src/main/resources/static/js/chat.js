$(() => {
    let stompClient = null;

    function updateUI(isConnected) {
        $('#username').prop('disabled', isConnected);
        $('#connectBtn').prop('disabled', isConnected);
        $('#sendBtn').prop('disabled', !isConnected);
        $('#disconnectBtn').prop('disabled', !isConnected);
        $('#username').prop('disabled', isConnected);
        if (isConnected) {
            $('#messages').text('');
        }
    }

    function onMessage(message) {
        const body = JSON.parse(message.body);
        $(`<p>${body.sender} (${body.timestamp}): ${body.text}</p>`).appendTo($('#messages'));
    }

    function onConnect() {
        updateUI(true);
        stompClient.subscribe('/main-room/messages', onMessage)
        stompClient.subscribe(`/private-room/user${getSocketId()}`, onMessage)
    }

    function getSocketId() {
        return stompClient.ws._transport.url
            .replace('ws://localhost:8080/chat/', '')
            .replace('/websocket', '')
            .replace(/^[0-9]+\//,'');
    }

    function connect() {
        const socket = new SockJS("/chat")
        stompClient = Stomp.over(socket);
        stompClient.connect({user: $('#username').val()}, onConnect);
    }

    function disconnect() {
        updateUI(false);
        stompClient.disconnect();
    }

    function send() {
        const message = JSON.stringify({
            sender: $('#username').val(),
            recipient: $('#recipient').val() || 'all',
            text: $('#message').val()
        });
        stompClient.send('/ws/chat', {}, message);
    }

    $('#connectBtn').click(connect);
    $('#disconnectBtn').click(disconnect);
    $('#sendBtn').click(send);
});