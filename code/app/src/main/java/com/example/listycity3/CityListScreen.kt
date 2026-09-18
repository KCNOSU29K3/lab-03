package com.example.listycity3

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listycity3.ui.theme.ListyCity3Theme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width

import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import kotlin.coroutines.coroutineContext

@Composable
fun CityListScreen(
    cities: List<City>,
    onAddCity: (City) -> Unit,
    onUpdateCity: (City, City) -> Unit,
    modifier: Modifier = Modifier
) {
    var newCityName by remember { mutableStateOf("") }
    var newProvinceName by remember { mutableStateOf("") }
    var showAddCityFields by remember { mutableStateOf(false) }
    Column(modifier = Modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            FloatingActionButton(
                modifier = Modifier.padding(16.dp),
                onClick = {
                    showAddCityFields = !showAddCityFields
                }
            ) {
                Text("+")
            }
        }
        if (showAddCityFields) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = newCityName,
                    onValueChange = { newCityName = it },
                    label = { Text("City") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = newProvinceName,
                    onValueChange = { newProvinceName = it },
                    label = { Text("Province") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    modifier = Modifier.padding(vertical = 12.dp),
                    onClick = {
                        if (newCityName.isNotBlank() && newProvinceName.isNotBlank()) {
                            onAddCity(
                                City(
                                    name = newCityName,
                                    province = newProvinceName
                                )
                            )
                            newCityName = ""
                            newProvinceName = ""
                            showAddCityFields = false
                        }
                    }
                ) {
                    Text("Add City")
                }
            }
        }
        LazyColumn(modifier = modifier.fillMaxSize()) {
            itemsIndexed(cities) { index, city ->
                CityRow(city = city, updateCity = onUpdateCity)

                if (index < cities.lastIndex) {
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun CityRow(city: City, updateCity: (City, City) -> Unit) {
    var currentCity by remember { mutableStateOf(city) }

    var updatedCityName by remember {mutableStateOf(currentCity.name)}
    var updatedCityProvince by remember {mutableStateOf(currentCity.province)}

    var makeRowEditable by remember {mutableStateOf(false)}
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        if (!makeRowEditable) {
            Text(
                text = currentCity.name,
                fontSize = 30.sp,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = currentCity.province,
                fontSize = 30.sp,
                modifier = Modifier.weight(1f)
            )
            FloatingActionButton(
                modifier = Modifier.padding(16.dp),
                onClick = {
                    makeRowEditable = !makeRowEditable
                }
            ) {
                Text("Edit")
            }

        } else {
            OutlinedTextField(
                value = updatedCityName,
                onValueChange = {updatedCityName = it},
                label = { Text("Updated Name") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(2.dp))
            OutlinedTextField(
                value = updatedCityProvince,
                onValueChange = {updatedCityProvince = it},
                label = { Text("Updated Province") },
                modifier = Modifier.weight(1f)
            )
            Column() {
                FloatingActionButton(
                    modifier = Modifier.padding(16.dp),
                    onClick = {
                        makeRowEditable = false
                    }
                ) {
                    Text("Cancel")
                }
                FloatingActionButton(
                    modifier = Modifier.padding(16.dp),
                    onClick = {
                        // we don't actually update the rows here, because
                        // android compose will reload the UI
                        // the second the update triggers through updateCity
                        // update: guess it won't?????
                        val newCity = City(updatedCityName, updatedCityProvince)
                        updateCity(currentCity, newCity)

                        // set the thing. this will force a recomp.
                        currentCity = newCity

                        updatedCityName = currentCity.name
                        updatedCityProvince = currentCity.province
                        makeRowEditable = false
                    }
                ) {
                    Text("Confirm")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CityListScreenPreview() {
    ListyCity3Theme {
        CityListScreen(
            cities = listOf(
                City("Edmonton", "AB"),
                City("Vancouver", "BC"),
                City("Calgary", "AB")
            ),
            onAddCity = {},
            onUpdateCity = {old, new -> {}},
        )
    }
}