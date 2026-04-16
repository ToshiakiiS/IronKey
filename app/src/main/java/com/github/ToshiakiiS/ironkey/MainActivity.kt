package com.github.ToshiakiiS.ironkey

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.ToshiakiiS.ironkey.ui.theme.IronKeyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IronKeyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    IronKeyForm(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun IronKeyForm(modifier: Modifier = Modifier) {

    val context = LocalContext.current
    var generatedPassword by remember { mutableStateOf("") }
    var MaxCharacters by remember { mutableStateOf(8) }
    var passwordType by remember { mutableStateOf(PasswordType.PIN) }
    var isEditable by remember { mutableStateOf(false) }
// Checkboxes
    var includeUppercase by rememberSaveable {
        mutableStateOf(true) }
    var includeLowercase by rememberSaveable {
        mutableStateOf(true) }
    var includeNumbers by rememberSaveable {
        mutableStateOf(true) }
    var includeSymbols by rememberSaveable {
        mutableStateOf(false) }
    var passwordComplexity by remember { mutableStateOf(PasswordComplexity.MEDIUM) }


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.ironmanface),
            contentDescription = "Logo do app",
            modifier = Modifier
                .size(150.dp)
                //mesma coisa de
                //.width(150.dp)
                //.height(150.dp)
                .clip(CircleShape)
                .align(Alignment.CenterHorizontally),
            contentScale = ContentScale.Crop
        )
        Text(
            text = "Senhas Seguras",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold,
            maxLines = 2,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(8.dp)
        ) {
            Column {
                OutlinedTextField(
                    value = generatedPassword,
                    onValueChange = { newPassword ->
                        if (newPassword.length <= MaxCharacters)
                            generatedPassword = newPassword
                    },
                    enabled = isEditable,
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Password"
                        )
                    },
                    trailingIcon = {
                        if (generatedPassword.isNotEmpty()) {
                            Icon(
                                imageVector = Icons.Filled.ContentCopy,
                                contentDescription = "Copiar",
                                modifier = Modifier.clickable {
                                    copyPassword(context, generatedPassword)
                                }
                            )
                        }
                    }
                )

                Text(
                    "${generatedPassword.length} / $MaxCharacters",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 8.dp, top = 4.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))

                Text("Tipo de senha")

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()

                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { passwordType = PasswordType.PIN }
                    ) {
                        RadioButton(
                            selected = passwordType == PasswordType.PIN,
                            onClick = {passwordType = PasswordType.PIN}
                        )
                        Text("PIN")
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { passwordType = PasswordType.STANDARD }
                    ) {
                        RadioButton(
                            selected = passwordType == PasswordType.STANDARD,
                            onClick = {passwordType = PasswordType.STANDARD}
                        )
                        Text("Padrão")
                    }
                }

                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Icon(imageVector = if (isEditable) Icons.Default.LockOpen else Icons.Filled.Lock,
                        contentDescription = ""
                    )
                    Text("Permitir editar senha?", modifier = Modifier.padding(horizontal = 8.dp))
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(checked = isEditable, onCheckedChange = {isEditable = it})
                }
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))


                if(isEditable) {
                    Text("Complexidade da senha")
                    PasswordComplexityDropdown(selectedComplexity = passwordComplexity) {
                        passwordComplexity = it
                        MaxCharacters = (passwordComplexity.maxLength + passwordComplexity.minLength)/2

                    }
                    if (passwordType == PasswordType.STANDARD) {
                        Text("Tamanho da Senha ${MaxCharacters}")
                        Slider(
                            value = MaxCharacters.toFloat(),
                            onValueChange = {MaxCharacters = it.toInt()},
                            valueRange = passwordComplexity.minLength.toFloat() .. passwordComplexity.maxLength.toFloat(),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment
                        = Alignment.CenterVertically) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { includeUppercase = !includeUppercase }
                            ) {
                                Checkbox(
                                    checked = includeUppercase,
                                    onCheckedChange = { includeUppercase = it }
                                )
                                Text("Maiúsculas")
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { includeLowercase = !includeLowercase }
                            ) {
                                Checkbox(
                                    checked = includeLowercase,
                                    onCheckedChange = { includeLowercase = it }
                                )
                                Text("Minúsculas")
                            }
                        }
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { includeNumbers = !includeNumbers }
                            ) {
                                Checkbox(checked = includeNumbers,
                                    onCheckedChange = { includeNumbers = it }
                                )
                                Text("Números")
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { includeSymbols = !includeSymbols }
                            ) {
                                Checkbox(
                                    checked = includeSymbols,
                                    onCheckedChange = { includeSymbols = it }
                                )
                                Text("Símbolos")
                            }
                        }
                    }

                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        val generator : PasswordGenerator = if (passwordType == PasswordType.PIN) {
                            PinPasswordGenerator()
                        } else StandardPasswordGenerator()
                        generatedPassword = generator.generate(MaxCharacters)
                    }
                ) { Text("Gerar Senha") }

            }

        }
    }
}


@Preview(showBackground = true)
@Composable
private fun IronManFormPreview() {
    IronKeyForm()
}

fun copyPassword(context: Context, password: String) {
    val clipboardManager =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as
                ClipboardManager
    val clip = ClipData.newPlainText("Senha", password)
    clipboardManager.setPrimaryClip(clip)
    Toast.makeText(
        context, "Senha copiada!",
        Toast.LENGTH_SHORT
    ).show()
}

enum class PasswordType {
    PIN,
    STANDARD
}

enum class PasswordComplexity(
    val title: String,
    val minLength: Int,
    val maxLength: Int
) {
    LOW("Baixa", 4, 6),
    MEDIUM("Média", 4, 10),
    HIGH("Alta", 4, 16)
}

interface PasswordGenerator {
    fun generate(length: Int): String
}

class PinPasswordGenerator : PasswordGenerator {
    override fun generate(length: Int): String {
        val digits = ('0' .. '9')
        return (1..length)
            .map { digits.random() }
            .joinToString("")
    }
}
class StandardPasswordGenerator(
    private val includeUppercase: Boolean = true,
    private val includeLowercase: Boolean = true,
    private val includeNumbers: Boolean = true,
    private val includeSymbols: Boolean = true
) : PasswordGenerator {
    override fun generate(length: Int): String {
        val chars = buildList<Char> {
            if (includeUppercase) addAll('A'..'Z')
            if (includeLowercase) addAll('a'..'z')
            if (includeNumbers) addAll('0'..'9')
            if (includeSymbols)
                addAll("!@#\$%&*()_-+=<>?".toList())
        }
        if (chars.isEmpty()) return ""
        return (1..length)
            .map { chars.random() }
            .joinToString("")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordComplexityDropdown(
    selectedComplexity: PasswordComplexity,
    onComplexitySelected: (PasswordComplexity) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Spacer(modifier = Modifier.height(20.dp))
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedComplexity.title,
            onValueChange = {},
            readOnly = true,
            label = { Text("Complexidade da senha") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded)},
            modifier = Modifier.fillMaxWidth().menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            PasswordComplexity.entries.forEach { complexity ->
                DropdownMenuItem(
                    text = { Text(complexity.title) },
                    onClick = {
                        onComplexitySelected(complexity)
                        expanded = false
                    }
                )
            }
        }


    }
    
}
