package com.example.resif

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

import com.example.resif.ui.theme.ResIFTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ResIFTheme {
                ResIFApp()
            }
        }
    }
}

// Navigation Enum for type safety
enum class Screen {
    Welcome,
    Login,
    Register,
    Main
}

// Bottom Navigation Item Data Class
data class BottomNavItem(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@Composable
fun ResIFApp() {
    var currentScreen by remember { mutableStateOf(Screen.Welcome) }

    Surface(modifier = Modifier.fillMaxSize()) {
        when (currentScreen) {
            Screen.Welcome -> WelcomeScreen(
                onLoginClick = { currentScreen = Screen.Login },
                onRegisterClick = { currentScreen = Screen.Register }
            )
            Screen.Login -> LoginScreen(
                onLoginSuccess = { currentScreen = Screen.Main },
                onRegisterClick = { currentScreen = Screen.Register }
            )
            Screen.Register -> RegisterScreen(
                onRegisterSuccess = { currentScreen = Screen.Main },
                onLoginClick = { currentScreen = Screen.Login }
            )
            Screen.Main -> MainScreen(
                onLogoutClick = { currentScreen = Screen.Welcome }
            )
        }
    }
}

@Composable
fun MainScreen(onLogoutClick: () -> Unit) {
    var selectedRoute by remember { mutableStateOf("home") }

    val bottomNavItems = listOf(
        BottomNavItem("home", Icons.Filled.Home, Icons.Outlined.Home),
        BottomNavItem("booking", Icons.Filled.Search, Icons.Outlined.Search),
        BottomNavItem("history", Icons.Filled.Bookmark, Icons.Outlined.BookmarkBorder),
        BottomNavItem("profile", Icons.Filled.Person, Icons.Outlined.PersonOutline)
    )

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                items = bottomNavItems,
                selectedRoute = selectedRoute,
                onItemClick = { route -> selectedRoute = route }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedRoute) {
                "home" -> HomeScreen(onLogoutClick = onLogoutClick)
                "booking" -> BookingScreen(onLogoutClick = onLogoutClick)
                "history" -> HistoryScreen(onLogoutClick = onLogoutClick)
                "profile" -> ProfileScreen(onLogoutClick = onLogoutClick)
            }
        }
    }
}

@Composable
fun WelcomeScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF20469B),
                        Color(0xFF0B1835)
                    )
                )
            )
    ) {
        // ======== Logo + ResIF Centered =========
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 48.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Card(
                    modifier = Modifier.size(66.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E3A8A))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo ITS",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Text(
                    text = "ResIF",
                    color = Color.White,
                    fontSize = 60.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // ======== Buttons at Bottom =========
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 48.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onRegisterClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(66.dp)
                    .padding(bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(2.dp, Color.White)
            ) {
                Text(
                    text = "REGISTER",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 5.sp
                )
            }

            Button(
                onClick = onLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(66.dp)
                    .padding(bottom = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF1F1F1)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "LOGIN",
                    color = Color(0xFF1A47A8),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 5.sp
                )
            }
        }
    }
}

@Composable
fun ProfileScreen(
    onLogoutClick: () -> Unit = {}
) {
    val deepBlue = Color(0xFF20469B)
    val navyBlack = Color(0xFF0B1835)
    val lightGray = Color(0xFFEBEBEB)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        // Header Profile Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                .background(
                    Brush.verticalGradient(colors = listOf(deepBlue, navyBlack))
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 60.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Profile Icon
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(
                            Color.White.copy(alpha = 0.2f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AccountCircle,
                        contentDescription = "Profile Icon",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Hi, Welcome!",
                        color = Color.White,
                        fontSize = 30.sp,
                        letterSpacing = 3.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Nabilah Atika",
                        color = Color(0xFF59A7FF),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        fontSize = 26.sp
                    )
                }

                // Notification Icon
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .border(width = 1.dp, color = Color.White, shape = CircleShape)
                        .clickable { /* Handle notification click */ }
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notification",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
                    .width(400.dp)
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = lightGray),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "Account Details",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )

                    // Full Name
                    ProfileDetailItem(
                        icon = Icons.Default.Person,
                        iconColor = deepBlue,
                        label = "Full Name",
                        value = "Nabilah Atika"
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Email
                    ProfileDetailItem(
                        icon = Icons.Default.Email,
                        iconColor = deepBlue,
                        label = "Email",
                        value = "inielava26@gmail.com"
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    Button(
                        onClick = onLogoutClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .padding(horizontal = 13.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xCCFF5959)
                        ),
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Text(
                            text = "Logout",
                            color = Color(0xFFFFFFFF),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun ProfileDetailItem(
    icon: ImageVector,
    iconColor: Color,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Icon
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(iconColor.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconColor,
                modifier = Modifier.size(27.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Label and Value
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                color = iconColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = 16.sp,
                color = Color.Black,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

data class RoomDetail(
    val code: String,
    val name: String,
    val technician: String = "Junaidy Abdillah",
    val phone: String = "087855224496"
)

@Composable
fun HistoryScreen(
    onLogoutClick: () -> Unit = {}
) {
    val deepBlue = Color(0xFF20469B)
    val navyBlack = Color(0xFF0B1835)

    // Sample booking history data
    val bookings = listOf(
        BookingHistory(
            date = "10 Juni 2025",
            time = "18.00–20.00",
            room = "Ruang Kelas IF-104",
            status = "Pending"
        ),
        BookingHistory(
            date = "9 Juni 2025",
            time = "18.00–20.00",
            room = "Ruang Kelas IF-107",
            status = "Approved"
        ),
        BookingHistory(
            date = "9 Juni 2025",
            time = "18.00–20.00",
            room = "Ruang Kelas IF-103",
            status = "Rejected"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                .background(
                    Brush.verticalGradient(colors = listOf(deepBlue, navyBlack))
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 60.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Profile Icon
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(
                            Color.White.copy(alpha = 0.2f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AccountCircle,
                        contentDescription = "Profile Icon",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Hi, Welcome!",
                        color = Color.White,
                        fontSize = 30.sp,
                        letterSpacing = 3.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Nabilah Atika",
                        color = Color(0xFF59A7FF),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        fontSize = 26.sp
                    )
                }

                // Notification Icon
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .border(width = 1.dp, color = Color.White, shape = CircleShape)
                        .clickable { /* Handle notification click */ }
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notification",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // Content area - History List
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Booking History",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(bookings) { booking ->
                    BookingHistoryCard(booking)
                }
            }
        }
    }
}

// Data class for booking history
data class BookingHistory(
    val date: String,
    val time: String,
    val room: String,
    val status: String // "Pending", "Approved", or "Rejected"
)

@Composable
fun BookingHistoryCard(booking: BookingHistory) {
    val statusColor = when (booking.status) {
        "Approved" -> Color(0xFF4CAF50) // Green
        "Rejected" -> Color(0xFFF44336) // Red
        else -> Color(0xFFFFC107) // Amber/Yellow for Pending
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(BorderStroke(1.dp, Color(0xFF20469B)), shape = RoundedCornerShape(14.dp)),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Date and Time
            Text(
                text = "${booking.date} ${booking.time}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Room Name
            Text(
                text = booking.room,
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Status Chip
            Box(
                modifier = Modifier
                    .wrapContentWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(statusColor.copy(alpha = 0.2f))
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = booking.status,
                    color = statusColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun BookingScreen(
    onLogoutClick: () -> Unit = {}
) {
    val deepBlue = Color(0xFF20469B)
    val navyBlack = Color(0xFF0B1835)
    val context = LocalContext.current

    // State untuk menampung room yang dipilih
    var selectedRoom by remember { mutableStateOf<RoomDetail?>(null) }
    // State untuk form booking
    var bookingDate by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }

    // List of all rooms
    val rooms = listOf(
        RoomDetail("AJK", "Laboratorium Arsitektur Jaringan Komputer"),
        RoomDetail("AULA", "Aula"),
        RoomDetail("GIGa", "Laboratorium Grafika, Interaksi dan Game"),
        RoomDetail("IF IUP", "Ruang Kelas IUP"),
        RoomDetail("IF-101", "Ruang Kelas"),
        RoomDetail("IF-102", "Ruang Kelas"),
        RoomDetail("IF-103", "Ruang Kelas"),
        RoomDetail("IF-104", "Ruang Kelas"),
        RoomDetail("IF-105A", "Ruang Kelas"),
        RoomDetail("IF-105B", "Ruang Kelas"),
        RoomDetail("IF-106", "Ruang Kelas"),
        RoomDetail("IF-107A", "Ruang Kelas"),
        RoomDetail("IF-107B", "Ruang Kelas"),
        RoomDetail("IF-108", "Ruang Kelas"),
        RoomDetail("IF-109", "LAB PASCA BESAR"),
        RoomDetail("IF-110", "LAB PASCA KECIL"),
        RoomDetail("IF-111", "Pascasarjana"),
        RoomDetail("IF-112", "Pascasarjana"),
        RoomDetail("IF-113", "Ruang Kelas"),
        RoomDetail("KCV", "Laboratorium Komputasi Cerdas Visi"),
        RoomDetail("LP1", "Laboratorium Pemrograman 1"),
        RoomDetail("LP2", "Laboratorium Pemrograman 2"),
        RoomDetail("NCC", "Laboratorium Net-Centric Computing"),
        RoomDetail("RS", "Ruang Sidang"),
        RoomDetail("PKT", "Laboratorium Pemodelan Komputasi Terapan"),
        RoomDetail("RAPAT1", "Ruang Rapat 1"),
        RoomDetail("RAPAT2", "Ruang Rapat 2"),
        RoomDetail("RPL", "Laboratorium Rekayasa Perangkat Lunak"),
        RoomDetail("RTV", "Ruang TV")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header Profile Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                .background(
                    Brush.verticalGradient(colors = listOf(deepBlue, navyBlack))
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 60.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Profile Icon
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(
                            Color.White.copy(alpha = 0.2f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AccountCircle,
                        contentDescription = "Profile Icon",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Hi, Welcome!",
                        color = Color.White,
                        fontSize = 30.sp,
                        letterSpacing = 3.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Nabilah Atika",
                        color = Color(0xFF59A7FF),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        fontSize = 26.sp
                    )
                }

                // Notification Icon
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .border(width = 1.dp, color = Color.White, shape = CircleShape)
                        .clickable { /* Handle notification click */ }
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notification",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // Content area - Room List
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Text(
                text = "Room Booking",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 8.dp)
            )

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = rooms,
                    key = { it.code }
                ) { room ->
                    RoomCard(
                        room = room,
                        onCardClick = { selectedRoom = room }
                    )
                }
            }
        }
    }

    // Dialog detail room
    selectedRoom?.let { room ->
        RoomDetailDialog(
            room = room,
            bookingDate = bookingDate,
            startTime = startTime,
            endTime = endTime,
            onDateChange = { bookingDate = it },
            onStartTimeChange = { startTime = it },
            onEndTimeChange = { endTime = it },
            onBooking = {
                // Logic untuk booking
                Toast.makeText(context, "Booking submitted for ${room.code}", Toast.LENGTH_SHORT).show()
                // Reset states
                selectedRoom = null
                bookingDate = ""
                startTime = ""
                endTime = ""
            },
            onDismiss = { selectedRoom = null }
        )
    }
}

@Composable
fun RoomCard(
    room: RoomDetail,
    onCardClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(1.dp, Color(0xFF20469B))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = room.code,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF20469B)
            )
            Text(
                text = room.name,
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun RoomDetailDialog(
    room: RoomDetail,
    bookingDate: String,
    startTime: String,
    endTime: String,
    onDateChange: (String) -> Unit,
    onStartTimeChange: (String) -> Unit,
    onEndTimeChange: (String) -> Unit,
    onBooking: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                Text(
                    text = "Room Details",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Room Info
                DetailItem("Room Code", room.code)
                DetailItem("Room Name", room.name)
                DetailItem("Technician", room.technician)
                DetailItem("Phone Number", room.phone)

                Spacer(modifier = Modifier.height(24.dp))

                // Booking Form
                Text(
                    text = "Date/Time",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = bookingDate,
                    onValueChange = onDateChange,
                    label = { Text("Date (e.g. 10 Juni 2025)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = startTime,
                        onValueChange = onStartTimeChange,
                        label = { Text("Start Time") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = endTime,
                        onValueChange = onEndTimeChange,
                        label = { Text("End Time") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onBooking,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    enabled = bookingDate.isNotBlank() &&
                            startTime.isNotBlank() &&
                            endTime.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF20469B)
                    )
                ) {
                    Text("Book Now", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun DetailItem(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false,
    isPasswordVisible: Boolean = false,
    onPasswordVisibilityChange: () -> Unit = {}
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(fontSize = 20.sp),
        placeholder = {
            Text(
                text = placeholder,
                fontSize = 20.sp,
                color = Color.Gray
            )
        },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 8.dp)
            )
        },
        trailingIcon = if (isPassword) {
            {
                val icon = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = onPasswordVisibilityChange, modifier = Modifier.padding(end = 8.dp)) {
                    Icon(imageVector = icon, contentDescription = "Toggle password visibility")
                }
            }
        } else null,
        visualTransformation = if (isPassword && !isPasswordVisible) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
fun BottomNavigationBar(
    items: List<BottomNavItem>,
    selectedRoute: String,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = Color(0xFF0A0C2D),
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            val isSelected = item.route == selectedRoute
            NavigationBarItem(
                selected = isSelected,
                onClick = { onItemClick(item.route) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.route.replaceFirstChar { it.uppercase() },
                        modifier = Modifier.size(28.dp)
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    unselectedIconColor = Color.Gray,
                    indicatorColor = Color.Transparent // No indicator circle
                )
            )
        }
    }
}

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onLoginClick: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 100.dp, start = 16.dp, end = 16.dp)
        ) {
            Text(
                text = "CREATE AN ACCOUNT",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Join us to get started",
                fontSize = 18.sp,
                color = Color.Gray,
                letterSpacing = 1.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Blue Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.70f)
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(topStart = 70.dp, topEnd = 70.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF20469B),
                            Color(0xFF000000)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Handle
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(5.dp)
                        .background(Color.White.copy(alpha = 0.8f), RoundedCornerShape(3.dp))
                )
                Spacer(modifier = Modifier.height(30.dp))

                // Input Fields
                CustomTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    placeholder = "Full Name",
                    leadingIcon = Icons.Default.Person
                )

                Spacer(modifier = Modifier.height(16.dp))

                CustomTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "Email",
                    leadingIcon = Icons.Default.Email,
                    keyboardType = KeyboardType.Email
                )

                Spacer(modifier = Modifier.height(16.dp))

                CustomTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = "Password",
                    leadingIcon = Icons.Default.Lock,
                    isPassword = true,
                    isPasswordVisible = isPasswordVisible,
                    onPasswordVisibilityChange = { isPasswordVisible = !isPasswordVisible }
                )

                Spacer(modifier = Modifier.height(16.dp))

                CustomTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    placeholder = "Confirm Password",
                    leadingIcon = Icons.Default.Lock,
                    isPassword = true,
                    isPasswordVisible = isConfirmPasswordVisible,
                    onPasswordVisibilityChange = { isConfirmPasswordVisible = !isConfirmPasswordVisible }
                )

                Spacer(modifier = Modifier.height(30.dp))

                // Register Button
                Button(
                    onClick = {
                        if (fullName.isNotEmpty() && email.isNotEmpty() &&
                            password.isNotEmpty() && confirmPassword.isNotEmpty()
                        ) {
                            if (password == confirmPassword) {
                                Toast.makeText(context, "Registration successful!", Toast.LENGTH_SHORT).show()
                                onRegisterSuccess()
                            } else {
                                Toast.makeText(context, "Passwords don't match!", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF1F1F1)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "REGISTER",
                        color = Color(0xFF1A47A8),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 4.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Already have an account? ",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 16.sp
                    )
                    TextButton(
                        onClick = onLoginClick,
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "Login here",
                            color = Color(0xFF7BB3FF),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 100.dp, start = 16.dp, end = 16.dp)
        ) {
            Text(
                text = "WELCOME BACK",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 4.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Please login to your account",
                fontSize = 18.sp,
                color = Color.Gray,
                letterSpacing = 1.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Blue Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.70f)
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(topStart = 70.dp, topEnd = 70.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF20469B),
                            Color(0xFF000000)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Handle
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(5.dp)
                        .background(Color.White.copy(alpha = 0.8f), RoundedCornerShape(3.dp))
                )
                Spacer(modifier = Modifier.height(30.dp))

                // Email Field
                CustomTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "Email",
                    leadingIcon = Icons.Default.Email,
                    keyboardType = KeyboardType.Email
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Password Field
                CustomTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = "Password",
                    leadingIcon = Icons.Default.Lock,
                    isPassword = true,
                    isPasswordVisible = isPasswordVisible,
                    onPasswordVisibilityChange = { isPasswordVisible = !isPasswordVisible }
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Forgot Password?",
                    color = Color(0xFF7BB3FF),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.End)
                        .clickable { /* TODO: Add forgot password logic */ }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Login Button
                Button(
                    onClick = {
                        if (email.isNotEmpty() && password.isNotEmpty()) {
                            Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()
                            onLoginSuccess()
                        } else {
                            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF1F1F1)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "LOGIN",
                        color = Color(0xFF1A47A8),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 4.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "------------- Or continue with -------------",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(Color(0xFFF0F0F0), shape = RoundedCornerShape(16.dp))
                            .clickable { },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.google),
                            contentDescription = "Google",
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(Color(0xFFF0F0F0), shape = RoundedCornerShape(16.dp))
                            .clickable { },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.apple),
                            contentDescription = "Apple",
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Don't have an account? ",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 16.sp
                    )
                    TextButton(
                        onClick = onRegisterClick,
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "Register here",
                            color = Color(0xFF7BB3FF),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(
    onLogoutClick: () -> Unit
) {
    val deepBlue = Color(0xFF20469B)
    val navyBlack = Color(0xFF0B1835)

    // State untuk semua expandable sections
    var isKetentuanExpanded by remember { mutableStateOf(false) }
    var isDiagramExpanded by remember { mutableStateOf(false) }
    var isTatatertibExpanded by remember { mutableStateOf(false) }
    var isPeraturanExpanded by remember { mutableStateOf(false) }
    var isFasilitasExpanded by remember { mutableStateOf(false) }

    // Konten yang bisa di scroll
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.White)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                .background(
                    Brush.verticalGradient(colors = listOf(deepBlue, navyBlack))
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 60.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Profile Icon
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(
                            Color.White.copy(alpha = 0.2f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AccountCircle,
                        contentDescription = "Profile Icon",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Hi, Welcome!",
                        color = Color.White,
                        fontSize = 30.sp,
                        letterSpacing = 3.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Nabilah Atika",
                        color = Color(0xFF59A7FF),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        fontSize = 26.sp
                    )
                }

                // Notification Icon
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .border(width = 1.dp, color = Color.White, shape = CircleShape)
                        .clickable { /* Handle notification click */ }
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notification",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Menu Cards - All Expandable
        Column(
            modifier = Modifier.padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // 1. Ketentuan Peminjaman
            ExpandableCard(
                title = "Ketentuan Peminjaman",
                isExpanded = isKetentuanExpanded,
                onToggle = { isKetentuanExpanded = !isKetentuanExpanded }
            ) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    val ketentuanItems = listOf(
                        "Pastikan tanggal peminjaman tidak bertabrakan dengan acara lain.",
                        "Peminjam mengajukan permohonan peminjaman ruangan kepada Kepala Departemen Informatika ITS.",
                        "Persetujuan reservasi oleh Kepala Departemen berkoordinasi dengan Penanggung Jawab Ruangan didasarkan atas skala prioritas dan potensi gangguan (keamanan, kebisingan) dengan kegiatan waktu yang sama.",
                        "Setelah semua proses peminjaman ruangan disetujui, peminjam menghubungi Penanggung Jawab Ruangan untuk mendapatkan kunci:",
                        "Informasi dan jadwal setiap ruangan dapat dilihat di laman ruangan."
                    )

                    ketentuanItems.forEachIndexed { index, item ->
                        if (index == 3) {
                            // Item dengan sub-items
                            Text(
                                text = "${index + 1}. $item",
                                fontSize = 14.sp,
                                color = Color.Black,
                                lineHeight = 20.sp,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                            // Sub-items untuk penanggung jawab
                            val penanggungjawab = listOf(
                                "• Ruang Kelas - Kunto Hermono",
                                "• Ruang Aula - Jumali",
                                "• Lab Pemrograman 1 - Junaidy Abdillah",
                                "• Lab Pemrograman 2 - Edy Lukito"
                            )
                            penanggungjawab.forEach { subItem ->
                                Text(
                                    text = subItem,
                                    fontSize = 14.sp,
                                    color = Color.Black,
                                    lineHeight = 18.sp,
                                    modifier = Modifier.padding(start = 16.dp, top = 2.dp, bottom = 2.dp)
                                )
                            }
                        } else {
                            Text(
                                text = "${index + 1}. $item",
                                fontSize = 14.sp,
                                color = Color.Black,
                                lineHeight = 20.sp,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            // 2. Diagram Alur Peminjaman
            ExpandableCard(
                title = "Diagram Alur Peminjaman",
                isExpanded = isDiagramExpanded,
                onToggle = { isDiagramExpanded = !isDiagramExpanded }
            ) {
                Column(
                    modifier = Modifier.padding(top = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(
                                Color.Gray.copy(alpha = 0.1f),
                                RoundedCornerShape(12.dp)
                            )
                            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.sop),
                            contentDescription = "Diagram Alur Peminjaman",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }

            // 3. Tata Tertib
            ExpandableCard(
                title = "Tata Tertib",
                isExpanded = isTatatertibExpanded,
                onToggle = { isTatatertibExpanded = !isTatatertibExpanded }
            ) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    val rules = listOf(
                        "Penggunaan ruangan harus mendapat persetujuan dari Kepala Departemen Informatika ITS.",
                        "Pengajuan peminjaman maksimal 2 minggu sebelum pelaksanaan kegiatan.",
                        "Penggunaan ruang hanya diperbolehkan pada rentang waktu jam kerja (08:00 -18:00) di hari kerja, dan maksimal pukul 16:00 untuk hari Sabtu dan Minggu.",
                        "Pengguna atau Peminjam hanya dikhususkan untuk civitas akademika Jurusan Teknik Informatika ITS.",
                        "Pengguna ruang wajib melakukan pemeriksaan kondisi barang yang akan digunakan sebelum maupun sesudah digunakan untuk memastikan keadaan kondisi barang dalam keadaan baik.",
                        "Tidak dibenarkan meninggalkan ruang dalam keadaan kosong dan tidak terkunci.",
                        "Jika terjadi kerusakan inventaris ruang karena kelalaian/kecerobohan pemakaian maka yang bersangkutan diberi sanksi untuk:"
                    )

                    rules.forEachIndexed { index, rule ->
                        if (index == 6) {
                            // Item dengan sub-sanksi
                            Text(
                                text = "${index + 1}. $rule",
                                fontSize = 14.sp,
                                color = Color.Black,
                                lineHeight = 20.sp,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                            val sanksi = listOf(
                                "a. Memperbaiki alat tersebut apabila kerusakan tersebut dapat diperbaiki.",
                                "b. Mengganti dengan alat yang baru apabila kerusakan tersebut tidak bisa diperbaiki."
                            )
                            sanksi.forEach { sanksiItem ->
                                Text(
                                    text = sanksiItem,
                                    fontSize = 14.sp,
                                    color = Color.Black,
                                    lineHeight = 18.sp,
                                    modifier = Modifier.padding(start = 16.dp, top = 2.dp, bottom = 2.dp)
                                )
                            }
                        } else {
                            Text(
                                text = "${index + 1}. $rule",
                                fontSize = 14.sp,
                                color = Color.Black,
                                lineHeight = 20.sp,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            // 4. Peraturan Tambahan
            ExpandableCard(
                title = "Peraturan Tambahan",
                isExpanded = isPeraturanExpanded,
                onToggle = { isPeraturanExpanded = !isPeraturanExpanded }
            ) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    Text(
                        text = "AULA",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    val peraturanAula = listOf(
                        "Peserta kegiatan minimal 75 orang. Jika pada waktu pelaksanaan kegiatan peserta yang hadir kurang dari 75 orang, maka kegiatan tersebut harus bersedia dipindahkan di ruangan lain.",
                        "Dilarang masuk ke dalam ruang operator.",
                        "Dilarang melipat/memindahkan kursi tanpa seijin petugas aula.",
                        "Dilarang membawa kunci aula.",
                        "Setelah acara/kegiatan selesai, peminjam/pemakai diwajibkan untuk segera menghubungi petugas aula.",
                        "Dilarang membuang sampah/meninggalkan bekas makanan dan minuman di dalam ruang aula."
                    )

                    peraturanAula.forEachIndexed { index, rule ->
                        Text(
                            text = "${index + 1}. $rule",
                            fontSize = 14.sp,
                            color = Color.Black,
                            lineHeight = 20.sp,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }

            // 5. Fasilitas
            ExpandableCard(
                title = "Fasilitas",
                isExpanded = isFasilitasExpanded,
                onToggle = { isFasilitasExpanded = !isFasilitasExpanded }
            ) {
                Column(
                    modifier = Modifier.padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // AULA
                    FasilitasSection(
                        title = "AULA",
                        facilities = listOf(
                            "Kapasitas kursi 200 orang",
                            "Terhubung dengan Gygabytes INTRAnet ITS, dan INTERnet up to 7 GB (Shared with integra authentication)",
                            "Wifi dual band 2.4 GHz dan 5 Ghz",
                            "Proyektor",
                            "Audio System Supported"
                        )
                    )

                    // RUANG KELAS
                    FasilitasSection(
                        title = "RUANG KELAS",
                        facilities = listOf(
                            "Kapasitas kursi 40 orang",
                            "Terhubung dengan Gygabytes INTRAnet ITS, dan INTERnet up to 7 GB (Shared with integra authentication)",
                            "Wifi dual band 2.4 GHz dan 5 Ghz",
                            "Proyektor"
                        )
                    )

                    // LAB PEMROGRAMAN 1
                    FasilitasSection(
                        title = "LAB. PEMROGRAMAN 1",
                        facilities = listOf(
                            "Kapasitas kursi 75 orang",
                            "Terhubung dengan Gygabytes INTRAnet ITS, dan INTERnet up to 7 GB (Shared with integra authentication)",
                            "Wifi dual band 2.4 GHz dan 5 Ghz",
                            "Desktop PC (Processor i5, RAM 8GB, HDD 1TB)",
                            "Proyektor",
                            "Audio System Supported"
                        )
                    )

                    // LAB PEMROGRAMAN 2
                    FasilitasSection(
                        title = "LAB. PEMROGRAMAN 2",
                        facilities = listOf(
                            "Kapasitas kursi 54 orang",
                            "Terhubung dengan Gygabytes INTRAnet ITS, dan INTERnet up to 7 GB (Shared with integra authentication)",
                            "Wifi dual band 2.4 GHz dan 5 Ghz",
                            "Desktop PC (Processor i5, RAM 8GB, HDD 1TB)",
                            "Proyektor",
                            "Audio System Supported"
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ExpandableCard(
    title: String,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F4F4)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggle() }
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = if (isExpanded)
                        Icons.Default.ExpandLess
                    else
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = Color.Gray,
                    modifier = Modifier.size(32.dp)
                )
            }

            // Expandable Content
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(animationSpec = tween(durationMillis = 300)) + fadeIn(animationSpec = tween(durationMillis = 300)),
                exit = shrinkVertically(animationSpec = tween(durationMillis = 300)) + fadeOut(animationSpec = tween(durationMillis = 300))
            ) {
                content()
            }
        }
    }
}

@Composable
fun FasilitasSection(
    title: String,
    facilities: List<String>
) {
    Column {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        facilities.forEach { facility ->
            Row(modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)) {
                Text(
                    text = "• ",
                    fontSize = 14.sp,
                    color = Color.Black,
                    lineHeight = 20.sp,
                )
                Text(
                    text = facility,
                    fontSize = 14.sp,
                    color = Color.Black,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    ResIFTheme {
        WelcomeScreen(
            onLoginClick = {},
            onRegisterClick = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun LoginScreenPreview() {
    ResIFTheme {
        LoginScreen(
            onLoginSuccess = {},
            onRegisterClick = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun RegisterScreenPreview() {
    ResIFTheme {
        RegisterScreen(
            onRegisterSuccess = {},
            onLoginClick = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun MainScreenPreview() {
    ResIFTheme {
        MainScreen(onLogoutClick = {})
    }
}